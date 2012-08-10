package com.alk.battleBuffs;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
/**
 * 
 * @author alkarin
 *
 */
public class PermissionController {
    
    public static PermissionHandler ph;
    
    public PermissionController(){}
    
    public void loadPermissions(){
    	loadPermissionsPlugin();
    }
    
    static void loadPermissionsPlugin() {
        if (ph != null) {
            return;
        }
        
        Plugin permissionsPlugin = Bukkit.getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissionsPlugin == null) {
            System.out.println("Permission system not detected, defaulting to OP");
            return;
        }

        ph =  ((Permissions) permissionsPlugin).getHandler();
    }


	public static boolean hasPermission(Player player, String permission) {
		if (ph != null) return ph.has(player, permission) || player.isOp();
		else return player.isOp();
	}


}
