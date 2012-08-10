package com.alk.battleBuffs;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
/**
 * 
 * @author alkarin
 *
 */
public class ConfigController {
    public static FileConfiguration config;
	
    public static boolean getBoolean(String node) {return config.getBoolean(node, false);}
    public static  String getString(String node) {return config.getString(node,null);}
    public static  String getString(String node,String def) {return config.getString(node,def);}
    public static int getInt(String node,int i) {return config.getInt(node, i);}
    public static double getDouble(String node, double d) {return config.getDouble(node, d);}

	public ConfigController(){
		
	}
    public void setConfig(File f){
    	config = new YamlConfiguration();
    	try {config.load(f);} catch (Exception e) {e.printStackTrace();}
    	loadAll();
    }
    
    private void loadAll(){

    }

    
}
