package com.alk.battleBuffs;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleBuffs extends JavaPlugin{

	static private String pluginname; 
	static private String version;
	static private BattleBuffs plugin;
	private static Logger log = null;
	private BBCommandController commandController = new BBCommandController();
	
	@Override
	public void onEnable() {
		plugin = this;
		log = Bukkit.getLogger();
		PluginDescriptionFile pdfFile = plugin.getDescription();
		pluginname = pdfFile.getName();
		version = pdfFile.getVersion();
		
		PermissionController.loadPermissionsPlugin();

		info("[" + pluginname + " v" + version +"]"  + " enabled!");		
	}

	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}

		return commandController.handleCommand( player,cmd,commandLabel, args);
	}


	public static void info(String msg){log.info(Util.colorChat(msg));}
	public static void warn(String msg){log.warning(Util.colorChat(msg));}
	public static void err(String msg){log.severe(Util.colorChat(msg));}
}
