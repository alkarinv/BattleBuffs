package com.alk.battleBuffs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * 
 * @author alkarin
 *
 */
public class BBCommandController  {

		
	public boolean handleCommand(Player player, Command cmd, String commandLabel, String[] args) {
		String commandStr = cmd.getName().toLowerCase();
		for (String arg: args){
			if (!arg.matches("[a-zA-Z0-9_\\-/:,]*")) {
				sendMessage(player, "arguments can be only alphanumeric with underscores");
				return true;
			}
		}
		if (commandStr.equalsIgnoreCase("buff")){
			if (args.length > 0 && args[0].equalsIgnoreCase("list")){
				return buffList(player,args);
			}
			return buffPlayer(player, args);
		} else if (commandStr.equals("debuff")){
			return debuffPlayer(player,args);
		} else if (commandStr.equals("debuffall")){
			return debuffAllPlayer(player,args);
		}
		return true;
	}	


	private boolean buffList(Player p, String[] args) {
		StringBuilder sb = new StringBuilder("&e");
		boolean first = true;
		for (String s: EffectUtil.effectToName.values()){
			if (!first) sb.append("&e,");
			sb.append("&6" + s);
			first = false;
		}
		return sendMessage(p, sb.toString());
	}

	private boolean debuffAllPlayer(Player p, String[] args) {
		if (p != null && !p.isOp()){
			return true;
		}
		boolean debuffOther = false;
		Player player = null;
		if (args.length > 0){
			debuffOther = true;
			player = findPlayer(args[0]);
			if (player == null){
				return sendMessage(p, "&ePlayer " + args[0] +" can not be found");
			}
		} else {
			player = p;
		}
		EffectUtil.deEnchantAll(p);
		if (debuffOther){
			sendMessage(player, "&e you have been de-enchanted");
			return sendMessage(p,"&eYou have de-enchanted " + player.getName());
		}
		return sendMessage(p,"&eYou have de-enchanted yourself");
	}

	private boolean debuffPlayer(Player p, String[] args) {
		return debuffAllPlayer(p,args);
	}

	private boolean buffPlayer(Player p, String[] args) {
		if (p != null && !p.isOp()){
			return true;
		}
		if (args.length < 1){
			sendMessage(p,"&e/buff <playername> <effect name[:strength][:time]> <effect2 > ... [time: default 30s]");
			sendMessage(p,"&e/buff <effect name[:strength][:time]> <effect2 >... [time: default 30s]");
			sendMessage(p,"&eExample &6/buff speed:1 haste:2");
			return sendMessage(p,"&e/buff list, to see a list of buffs");
		}
		if (args[0] == null){
			return sendMessage(p,"&eYou need to specify an effect or user");
		}
		int sindex = 0;
		Player player = null;
		player = Bukkit.getPlayer(args[0]);
		if (player != null){
			sindex = 1;}
	
		Integer strength = 0;
		Integer time = 30;
		List<EffectWithArgs> ewas = new ArrayList<EffectWithArgs>();
		for (int i = sindex;i <args.length;i++){
			EffectWithArgs ewa = EffectUtil.parseArg(args[i]);
			if (ewa != null)
				ewas.add(ewa);
		}
		if (ewas.isEmpty()){
			return sendMessage(p,"&eCouldn't recognize buff &6" + args[sindex + 0] +"&e try &6/buff list");}

		if (args.length > sindex+1){try{ time = Integer.valueOf(args[args.length-1]);} catch (Exception e){}}
		
		boolean buffOther = true;
		if (player == null){
			player = p;
			buffOther = false;
		}
		String enchants = EffectUtil.enchantPlayer(player, ewas, strength, time);
		
		if (buffOther){
			sendMessage(player, "&e you have been enchanted with &6" + enchants);
			return sendMessage(p,"&eYou have enchanted &6" + player.getName() +"&e with &6" + enchants);
		}
		return sendMessage(p,"&eYou have enchanted yourself with &6" + enchants);
	}


	public static boolean sendMessage(Player p, String msg){
		if (msg == null)
			return false;
		if (p == null){
			BattleBuffs.info(msg);
		} else {
			p.sendMessage(Util.colorChat(msg));
		}
		return true;
	}

	private Player findPlayer(String name) {
		Server server =Bukkit.getServer();
		Player lastPlayer = server.getPlayer(name);
		if (lastPlayer != null) 
			return lastPlayer;

        Player[] online = server.getOnlinePlayers();
        for (Player player : online) {
            final String playerName = player.getName();
            if (playerName.equalsIgnoreCase(name)) {
                lastPlayer = player;
                break;
            }
            if (playerName.toLowerCase().indexOf(name.toLowerCase()) != -1) {
                if (lastPlayer != null) {
                    return null;}
                lastPlayer = player;
            }
        }

        return lastPlayer;
	}

}
