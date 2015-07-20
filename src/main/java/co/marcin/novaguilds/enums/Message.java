package co.marcin.novaguilds.enums;

import co.marcin.novaguilds.NovaGuilds;
import co.marcin.novaguilds.basic.NovaGuild;
import co.marcin.novaguilds.manager.MessageManager;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public enum Message {
	CHAT_PREFIX,
	CHAT_NOPERMISSIONS,
	CHAT_UNKNOWNCMD,
	CHAT_INVALIDPARAM,
	CHAT_CMDFROMCONSOLE,
	CHAT_ENTERINTEGER,
	CHAT_DELAYEDTELEPORT,
	CHAT_DELAYEDTPMOVED,
	CHAT_ERROROCCURED,
	CHAT_UPDATE,

	CHAT_BASIC_NEGATIVENUMBER,
	CHAT_BASIC_ON,
	CHAT_BASIC_OFF,

	CHAT_ADMIN_GUILD_TIMEREST_SET,
	CHAT_ADMIN_GUILD_LIST_HEADER,
	CHAT_ADMIN_GUILD_LIST_PAGE_HASNEXT,
	CHAT_ADMIN_GUILD_LIST_PAGE_NONEXT,
	CHAT_ADMIN_GUILD_LIST_ITEM,

	CHAT_ADMIN_GUILD_INACTIVE_UPDATED,
	CHAT_ADMIN_GUILD_INACTIVE_LIST_HEADER,
	CHAT_ADMIN_GUILD_INACTIVE_LIST_ITEM,
	CHAT_ADMIN_GUILD_INACTIVE_LIST_AGO,
	CHAT_ADMIN_GUILD_INACTIVE_LIST_NOW,

	CHAT_ADMIN_GUILD_BANK_PAID,
	CHAT_ADMIN_GUILD_BANK_WITHDREW,

	CHAT_ADMIN_GUILD_ABANDON,
	CHAT_ADMIN_GUILD_KICK_LEADER,
	CHAT_ADMIN_GUILD_INVITED,
	CHAT_ADMIN_GUILD_TELEPORTED_SELF,
	CHAT_ADMIN_GUILD_TELEPORTED_OTHER,

	CHAT_ADMIN_GUILD_SET_TAG,
	CHAT_ADMIN_GUILD_SET_POINTS,
	CHAT_ADMIN_GUILD_SET_LIVES,
	CHAT_ADMIN_GUILD_SET_NAME_ENTERNEWNAME,
	CHAT_ADMIN_GUILD_SET_NAME_SUCCESS,
	CHAT_ADMIN_GUILD_SET_LEADER_NOTINGUILD,
	CHAT_ADMIN_GUILD_SET_LEADER_ALREADYLEADER,
	CHAT_ADMIN_GUILD_SET_LEADER_SUCCESS,

	CHAT_ADMIN_REGION_BYPASS_TOGGLED_SELF,
	CHAT_ADMIN_REGION_BYPASS_TOGGLED_OTHER,
	CHAT_ADMIN_REGION_BYPASS_NOTIFYOTHER,

	CHAT_ADMIN_REGION_DELETE_SUCCESS,

	CHAT_ADMIN_REGION_TELEPORT_SELF,
	CHAT_ADMIN_REGION_TELEPORT_OTHER,
	CHAT_ADMIN_REGION_TELEPORT_NOTIFYOTHER,

	CHAT_ADMIN_SAVE_PLAYERS,
	CHAT_ADMIN_SAVE_GUILDS,
	CHAT_ADMIN_SAVE_REGIONS,
	CHAT_ADMIN_SAVE_ALL,
	CHAT_ADMIN_SAVE_AUTOSAVE,

	CHAT_RELOAD_RELOADING,
	CHAT_RELOAD_CONFIG,
	CHAT_RELOAD_MESSAGES,
	CHAT_RELOAD_MYSQL,
	CHAT_RELOAD_PLAYERS,
	CHAT_RELOAD_GUILDS,
	CHAT_RELOAD_REGIONS,
	CHAT_RELOAD_NEWMSGFILE,
	CHAT_RELOAD_GROUPS,
	CHAT_RELOAD_RELOADED,

	CHAT_PVP_TEAM,
	CHAT_PVP_ALLY,

	CHAT_NOGUILD,

	CHAT_RAID_RESTING,
	CHAT_RAID_PROTECTION,

	CHAT_GUILD_COMPASSTARGET_ON,
	CHAT_GUILD_COMPASSTARGET_OFF,
	CHAT_GUILD_NOGUILDS,
	CHAT_GUILD_FPVPTOGGLED,
	CHAT_GUILD_EFFECT_SUCCESS,

	CHAT_GUILD_ALLY_WAR,
	CHAT_GUILD_ALLY_SAMENAME,
	CHAT_GUILD_ALLY_ALREADYALLY,
	CHAT_GUILD_ALLY_ALREADYINVITED,
	CHAT_GUILD_ALLY_INVITED,
	CHAT_GUILD_ALLY_ACCEPTED,
	CHAT_GUILD_ALLY_NEWINVITE,
	CHAT_GUILD_ALLY_NOTIFYGUILD,
	CHAT_GUILD_ALLY_NOTIFYGUILDCANCELED,
	CHAT_GUILD_ALLY_CANCELED,

	CHAT_GUILD_COULDNOTFIND,
	CHAT_GUILD_HASNOREGION,
	CHAT_GUILD_ENTERNAME,
	CHAT_GUILD_ENTERTAG,
	CHAT_GUILD_NAMENOEXIST,
	CHAT_GUILD_NOTINGUILD,
	CHAT_GUILD_ABANDONED,
	CHAT_GUILD_NOTLEADER,
	CHAT_GUILD_NOTENOUGHTMONEY,
	CHAT_GUILD_SETHOME_SUCCESS,
	CHAT_GUILD_SETHOME_OUTSIDEREGION,
	CHAT_GUILD_SETHOME_OVERLAPS,
	CHAT_GUILD_HOME,
	CHAT_GUILD_HASREGIONALREADY,
	CHAT_CHAT_GUILD_JOINED,
	CHAT_GUILD_EXPLOSIONATREGION,
	CHAT_GUILD_KICKYOURSELF,
	CHAT_GUILD_TOOCLOSE,

	CHAT_GUILD_WAR_ALLY,
	CHAT_GUILD_WAR_YOURGUILDWAR,
	CHAT_GUILD_WAR_NOWARINV_SUCCESS,
	CHAT_GUILD_WAR_NOWARINV_NOTIFY,
	CHAT_GUILD_WAR_LIST_NOWARS,
	CHAT_GUILD_WAR_LIST_WARSHEADER,
	CHAT_GUILD_WAR_LIST_NOWARINVHEADER,
	CHAT_GUILD_WAR_LIST_ITEM,
	CHAT_GUILD_WAR_LIST_SEPARATOR,

	CHAT_PLAYER_ENTERNAME,
	CHAT_PLAYER_NOTEXISTS,
	CHAT_PLAYER_NOTONLINE,
	CHAT_PLAYER_HASGUILD,
	CHAT_PLAYER_HASNOGUILD,
	CHAT_PLAYER_ALREADYINVITED,
	CHAT_PLAYER_NOTINYOURGUILD,
	CHAT_PLAYER_INVITE_INVITED,
	CHAT_PLAYER_INVITE_LIST_HEADER,
	CHAT_PLAYER_INVITE_LIST_ITEM,
	CHAT_PLAYER_INVITE_LIST_SEPARATOR,
	CHAT_PLAYER_INVITE_LIST_NOTHING,
	CHAT_PLAYER_INVITE_NOTINVITED,
	CHAT_PLAYER_INVITE_NOTIFY,
	CHAT_PLAYER_INVITE_CANCELED,
	CHAT_PLAYER_INVITE_CANCELED_NOTIFY,

	CHAT_REGION_AREANOTSELECTED,

	CHAT_GUILD_VAULT_OUTSIDEREGION(true),
	CHAT_GUILD_VAULT_PLACE_SUCCESS(true),
	CHAT_GUILD_VAULT_PLACE_NOTLEADER(true),
	CHAT_GUILD_VAULT_DROP(true),
	CHAT_GUILD_VAULT_PLACE_EXISTS(true),
	CHAT_GUILD_VAULT_PLACE_DOUBLECHEST(true),
	CHAT_GUILD_VAULT_BREAK_NOTEMPTY(true),
	CHAT_GUILD_VAULT_BREAK_SUCCESS(true),
	CHAT_GUILD_VAULT_BREAK_NOTLEADER(true)

	;

	private static final MessageManager messageManager = NovaGuilds.getInst().getMessageManager();
	private boolean title = false;
	private String path = null;
	private HashMap<String,String> vars = new HashMap<>();
	private boolean prefix = true;

	Message(boolean title) {
		this.title = title;
	}

	Message() {

	}

	public boolean getTitle() {
		return title;
	}

	public String getPath() {
		if(path == null) {
			path = name().replace("_",".").toLowerCase();
		}

		return path;
	}

	public void send(CommandSender sender) {
		messageManager.sendMessagesMsg(sender, this, vars);
	}

	public Message vars(HashMap<String,String> vars) {
		this.vars = vars;
		return this;
	}

	public Message title(boolean title) {
		this.title = title;
		return this;
	}

	public Message prefix(boolean prefix) {
		this.prefix = prefix;
		return this;
	}

	public void broadcast(NovaGuild guild) {
		messageManager.broadcastGuild(guild, this, vars, prefix);
	}

	public void broadcast() {
		messageManager.broadcastMessage(this, vars);
	}

	public String get() {
		return messageManager.getMessagesString(path); //TODO replace with Message
	}

	public static String getOnOff(boolean b) {
		return b ? Message.CHAT_BASIC_ON.get() : Message.CHAT_BASIC_OFF.get();
	}
}
