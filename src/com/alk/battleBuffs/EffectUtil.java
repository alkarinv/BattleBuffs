package com.alk.battleBuffs;

import java.util.HashMap;
import java.util.List;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class EffectUtil {
	
	static final HashMap<MobEffectList,String> effectToName = new HashMap<MobEffectList,String>();
	static final HashMap<String,MobEffectList> nameToEffect= new HashMap<String,MobEffectList>();
	static{
		effectToName.put(MobEffectList.FASTER_MOVEMENT, "speed");
		  effectToName.put(MobEffectList.SLOWER_MOVEMENT, "slowness");
		  effectToName.put(MobEffectList.FASTER_DIG, "haste");
		  effectToName.put(MobEffectList.SLOWER_DIG,"slowswings");
		  effectToName.put(MobEffectList.INCREASE_DAMAGE, "strength");
		  effectToName.put(MobEffectList.HEAL, "heal");
		  effectToName.put(MobEffectList.HARM, "harm");
		  effectToName.put(MobEffectList.JUMP, "jump");
		  effectToName.put(MobEffectList.CONFUSION, "confusion");
		  effectToName.put(MobEffectList.REGENERATION, "regen");
		  effectToName.put(MobEffectList.RESISTANCE, "resistance");
		  effectToName.put(MobEffectList.FIRE_RESISTANCE, "fireresistance");
		  effectToName.put(MobEffectList.WATER_BREATHING, "waterbreathing");
		  effectToName.put(MobEffectList.INVISIBILITY, "invisibility");
		  effectToName.put(MobEffectList.BLINDNESS, "blindness");
		  effectToName.put(MobEffectList.NIGHT_VISION,"nightvision");
		  effectToName.put(MobEffectList.HUNGER, "hunger");
		  effectToName.put(MobEffectList.WEAKNESS, "weakness");
		  effectToName.put(MobEffectList.POISON, "poison");
		  nameToEffect.put("speed", MobEffectList.FASTER_MOVEMENT);
		  nameToEffect.put("slowness", MobEffectList.SLOWER_MOVEMENT);
		  nameToEffect.put("haste", MobEffectList.FASTER_DIG);
		  nameToEffect.put("slowdig", MobEffectList.SLOWER_DIG);
		  nameToEffect.put("strength", MobEffectList.INCREASE_DAMAGE);
		  nameToEffect.put("heal", MobEffectList.HEAL);
		  nameToEffect.put("harm", MobEffectList.HARM);
		  nameToEffect.put("jump", MobEffectList.JUMP);
		  nameToEffect.put("confusion", MobEffectList.CONFUSION);
		  nameToEffect.put("regeneration", MobEffectList.REGENERATION);
		  nameToEffect.put("resistance", MobEffectList.RESISTANCE);
		  nameToEffect.put("fireresistance", MobEffectList.FIRE_RESISTANCE);
		  nameToEffect.put("waterbreathing", MobEffectList.WATER_BREATHING);
		  nameToEffect.put("invisibility", MobEffectList.INVISIBILITY);
		  nameToEffect.put("blindness", MobEffectList.BLINDNESS);
		  nameToEffect.put("nightvision", MobEffectList.NIGHT_VISION);
		  nameToEffect.put("hunger", MobEffectList.HUNGER);
		  nameToEffect.put("weakness", MobEffectList.WEAKNESS);
		  nameToEffect.put("poison", MobEffectList.POISON);
	}

	public static MobEffectList getEffect(String buffName){
		buffName = buffName.toLowerCase();
		if (nameToEffect.containsKey(buffName))
			return nameToEffect.get(buffName);
		if (buffName.contains("slow")) return MobEffectList.SLOWER_MOVEMENT;
		else if (buffName.contains("fastdig")) return MobEffectList.FASTER_DIG;
		else if (buffName.contains("fasterdig")) return MobEffectList.FASTER_DIG;
		else if (buffName.contains("slowsw")) return MobEffectList.SLOWER_DIG;
		else if (buffName.contains("regen")) return MobEffectList.REGENERATION;
		else if (buffName.contains("resis")) return MobEffectList.RESISTANCE;
		else if (buffName.contains("waterb")) return MobEffectList.WATER_BREATHING;
		else if (buffName.contains("invis")) return MobEffectList.INVISIBILITY;
		else if (buffName.contains("blind")) return MobEffectList.BLINDNESS;
		return null;
	}

	public static void doEffect(EntityHuman player, MobEffectList mel, int time, int strength) {
		EntityLiving el = ((EntityLiving)player);
		el.addEffect(new MobEffect(mel.id, time * 20, strength));
	}

	public static String enchantPlayer(Player player, List<EffectWithArgs> ewas, final int strength, final int time){
		StringBuilder sb = new StringBuilder();
		EntityHuman eh = ((CraftPlayer)player).getHandle();
		boolean first = true;
		for (EffectWithArgs ewa : ewas){
			int str = ewa.strength != null ? ewa.strength : strength;
			int tim = ewa.time != null ? ewa.time : time;
			doEffect(eh, ewa.mel,tim,str);
			if (!first) sb.append(",");
			String commonName = effectToName.get(ewa.mel) +":" + (str+1);
			sb.append(commonName);
			first = false;
		}
		String enchants = sb.toString();
		return enchants;
	}
	
	public static EffectWithArgs parseArg(String arg) {
		arg = arg.replaceAll(",", ":");
		String split[] = arg.split(":");
		EffectWithArgs ewa = new EffectWithArgs();
		try {
			MobEffectList mel = getEffect(split[0]);
			if (mel == null)
				return null;
			ewa.mel = mel;
			if (split.length > 1){try{ewa.strength = Integer.valueOf(split[1]) -1;} catch (Exception e){}}
			if (split.length > 2){try{ewa.time = Integer.valueOf(split[2]);} catch (Exception e){}}
		} catch (Exception e){
			return null;
		}
		return ewa;
	}

	public static void deEnchantAll(Player p) {
		EntityHuman eh = ((CraftPlayer)p).getHandle();
		
		for (MobEffectList mel : EffectUtil.effectToName.keySet()){
	        if(eh.hasEffect(mel)){
	        	int mod = eh.getEffect(mel).getAmplifier();
	        	eh.addEffect(new MobEffect(mel.id, -1, mod+1));
	        }			
		}		
	}

}
