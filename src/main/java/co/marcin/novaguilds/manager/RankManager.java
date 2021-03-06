/*
 *     NovaGuilds - Bukkit plugin
 *     Copyright (C) 2015 Marcin (CTRL) Wieczorek
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package co.marcin.novaguilds.manager;

import co.marcin.novaguilds.NovaGuilds;
import co.marcin.novaguilds.basic.NovaGuild;
import co.marcin.novaguilds.basic.NovaPlayer;
import co.marcin.novaguilds.basic.NovaRank;
import co.marcin.novaguilds.enums.Config;
import co.marcin.novaguilds.enums.DataStorageType;
import co.marcin.novaguilds.enums.GuildPermission;
import co.marcin.novaguilds.enums.PreparedStatements;
import co.marcin.novaguilds.util.LoggerUtils;
import co.marcin.novaguilds.util.StringUtils;
import com.google.common.collect.Lists;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankManager {
	private final NovaGuilds plugin = NovaGuilds.getInstance();
	private final List<NovaRank> defaultRanks = new ArrayList<>();

	public void load() {
		//Load default ranks
		loadDefaultRanks();

		//Load ranks from storage
		int count = 0;
		if(Config.getManager().getDataStorageType() == DataStorageType.FLAT) {
			for(NovaGuild guild : plugin.getGuildManager().getGuilds()) {
				FileConfiguration guildData = plugin.getFlatDataManager().getGuildData(guild.getName());
				ConfigurationSection ranksConfigurationSection = guildData.getConfigurationSection("ranks");
				List<String> rankNamesList = new ArrayList<>();

				if(!guildData.isConfigurationSection("ranks")) {
					continue;
				}

				rankNamesList.addAll(ranksConfigurationSection.getKeys(false));

				for(String rankName : rankNamesList) {
					ConfigurationSection rankConfiguration = ranksConfigurationSection.getConfigurationSection(rankName);

					NovaRank rank = new NovaRank(0);
					rank.setName(rankName);

					List<String> permissionsStringList = rankConfiguration.getStringList("permissions");
					List<GuildPermission> permissionsList = new ArrayList<>();
					for(String permissionString : permissionsStringList) {
						permissionsList.add(GuildPermission.valueOf(permissionString));
					}
					rank.setPermissions(permissionsList);

					guild.addRank(rank);
					rank.setGuild(guild);

					for(String playerName : rankConfiguration.getStringList("members")) {
						NovaPlayer nPlayer = plugin.getPlayerManager().getPlayer(playerName);
						rank.addMember(nPlayer);
					}

					rank.setUnchanged();
					count++;
				}
			}
		}
		else {
			plugin.getDatabaseManager().mysqlReload();

			if(!plugin.getDatabaseManager().isConnected()) {
				LoggerUtils.info("Connection is not estabilished, stopping current action");
				return;
			}

			try {
				PreparedStatement statement = plugin.getDatabaseManager().getPreparedStatement(PreparedStatements.RANKS_SELECT);

				ResultSet res = statement.executeQuery();
				while(res.next()) {
					NovaRank rank = new NovaRank(res.getInt("id"));

					NovaGuild guild = plugin.getGuildManager().getGuildByName(res.getString("guild"));

					if(guild == null) {
						LoggerUtils.error("Failed to find guild: "+res.getString("name"));
						continue;
					}

					rank.setName(res.getString("name"));
					rank.setGuild(guild);

					for(String permName : StringUtils.jsonToList(res.getString("permissions"))) {
						rank.addPermission(GuildPermission.valueOf(permName));
					}

					for(String playerName : StringUtils.jsonToList(res.getString("members"))) {
						rank.addMember(plugin.getPlayerManager().getPlayer(playerName));
					}


					rank.setUnchanged();
					count++;
				}
			}
			catch(SQLException e) {
				LoggerUtils.exception(e);
			}
		}

		LoggerUtils.info("Loaded " + count + " ranks.");

		//Assing ranks to players
		assignRanks();
	}

	public void save() {
		if(Config.getManager().getDataStorageType() == DataStorageType.FLAT) {
			for(String guildName : plugin.getFlatDataManager().getGuildList()) {
				NovaGuild guild = NovaGuild.get(guildName);
				if(guild == null) {
					continue;
				}

				FileConfiguration guildData = plugin.getFlatDataManager().getGuildData(guildName);

				if(!guildData.isConfigurationSection("ranks") && guild.getRanks().size() > 0) {
					guildData.createSection("ranks");
				}

				ConfigurationSection ranksConfigurationSection = guildData.getConfigurationSection("ranks");
				List<String> rankList = new ArrayList<>(ranksConfigurationSection.getKeys(false));

				for(NovaRank rank : guild.getRanks()) {
					rankList.remove(rank.getName());

					if(!rank.isChanged()) {
						continue;
					}

					if(!ranksConfigurationSection.isConfigurationSection(rank.getName())) {
						ranksConfigurationSection.createSection(rank.getName());
					}

					List<String> memberNames = new ArrayList<>();
					for(NovaPlayer nPlayer : rank.getMembers()) {
						memberNames.add(nPlayer.getName());
					}

					ranksConfigurationSection.set(rank.getName()+".members", memberNames);

					List<String> permissionNamesList = new ArrayList<>();
					for(GuildPermission permission : rank.getPermissions()) {
						permissionNamesList.add(permission.name());
					}

					ranksConfigurationSection.set(rank.getName()+".permissions", permissionNamesList);
					rank.setUnchanged();
				}

				//remove deleted ranks TODO test
				for(String rankName : rankList) {
					ranksConfigurationSection.set(rankName, null);
				}

				try {
					guildData.save(plugin.getFlatDataManager().getGuildFile(guild.getName()));
				}
				catch(IOException e) {
					LoggerUtils.exception(e);
				}
			}
		}
		else {
			plugin.getDatabaseManager().mysqlReload();

			for(NovaGuild guild : plugin.getGuildManager().getGuilds()) {
				for(NovaRank rank : guild.getRanks()) {
					if(!rank.isChanged()) {
						continue;
					}

					if(rank.isNew()) {
						add(rank);
						continue;
					}

					List<String> memberNamesList = new ArrayList<>();
					for(NovaPlayer nPlayer : rank.getMembers()) {
						memberNamesList.add(nPlayer.getName());
					}

					List<String> permissionNamesList = new ArrayList<>();
					for(GuildPermission permission : rank.getPermissions()) {
						permissionNamesList.add(permission.name());
					}

					try {
						PreparedStatement preparedStatement = plugin.getDatabaseManager().getPreparedStatement(PreparedStatements.RANKS_UPDATE);
						preparedStatement.setString(1, rank.getName());
						preparedStatement.setString(2, guild.getName());
						preparedStatement.setString(3, JSONArray.toJSONString(permissionNamesList));
						preparedStatement.setString(4, JSONArray.toJSONString(memberNamesList));
						preparedStatement.setInt(5, rank.getId());
						preparedStatement.execute();

						rank.setUnchanged();
					}
					catch(SQLException e) {
						LoggerUtils.exception(e);
					}
				}
			}
		}
	}

	public void add(NovaRank rank) {
		if(Config.getManager().getDataStorageType() != DataStorageType.FLAT) {
			try {
				List<String> memberNamesList = new ArrayList<>();
				for(NovaPlayer nPlayer : rank.getMembers()) {
					memberNamesList.add(nPlayer.getName());
				}

				List<String> permissionNamesList = new ArrayList<>();
				for(GuildPermission permission : rank.getPermissions()) {
					permissionNamesList.add(permission.name());
				}

				PreparedStatement preparedStatement = plugin.getDatabaseManager().getPreparedStatement(PreparedStatements.RANKS_INSERT);
				preparedStatement.setString(1, rank.getName());
				preparedStatement.setString(2, rank.getGuild().getName());
				preparedStatement.setString(3, JSONArray.toJSONString(permissionNamesList));
				preparedStatement.setString(4, JSONArray.toJSONString(memberNamesList));
				preparedStatement.execute();

				ResultSet keys = preparedStatement.getGeneratedKeys();
				int id = 0;
				if(keys.next()) {
					id = keys.getInt(1);
				}
				if(id > 0) {
					rank.setId(id);
				}

				rank.setUnchanged();
			}
			catch(SQLException e) {
				LoggerUtils.exception(e);
			}
		}
	}

	public void delete(NovaRank rank) {
		if(Config.getManager().getDataStorageType() != DataStorageType.FLAT) {
			try {
				PreparedStatement preparedStatement = plugin.getDatabaseManager().getPreparedStatement(PreparedStatements.RANKS_DELETE);
				preparedStatement.setInt(1, rank.getId());
				preparedStatement.execute();

				rank.getGuild().removeRank(rank);

				for(NovaPlayer nPlayer : rank.getMembers()) {
					rank.removeMember(nPlayer);
				}
			}
			catch(SQLException e) {
				LoggerUtils.exception(e);
			}
		}
	}

	public void loadDefaultRanks() {
		NovaRank leaderRank = new NovaRank("Leader");
		leaderRank.setPermissions(Lists.newArrayList(GuildPermission.values()));
		defaultRanks.add(leaderRank);
		int count = 1;

		ConfigurationSection section = Config.GUILD_DEFAULTRANKS.getConfigurationSection();
		for(String rankName : section.getKeys(false)) {
			NovaRank rank = new NovaRank(rankName);

			for(String permName : section.getStringList(rankName)) {
				rank.addPermission(GuildPermission.valueOf(permName.toUpperCase()));
			}

			defaultRanks.add(rank);
			count++;
		}

		LoggerUtils.info("Loaded " + count + " default (guild) ranks.");
	}

	public boolean isDefaultRank(NovaRank rank) {
		return defaultRanks.contains(rank);
	}

	public List<NovaRank> getDefaultRanks() {
		return defaultRanks;
	}

	private void assignRanks() {
		for(NovaGuild guild : plugin.getGuildManager().getGuilds()) {
			for(NovaPlayer nPlayer : guild.getPlayers()) {
				if(nPlayer.getGuildRank() == null) {
					if(nPlayer.isLeader()) {
						nPlayer.setGuildRank(getDefaultRanks().get(0)); //TODO ???
					}
					else {
						//TODO
					}
				}
			}
		}
	}
}
