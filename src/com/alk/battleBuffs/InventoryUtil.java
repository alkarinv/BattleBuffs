package com.alk.battleBuffs;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/**
 * 
 * @author alkarin
 *
 */
public class InventoryUtil {
	
    public static int getItemAmountFromInventory(Inventory inv, ItemStack is) {
        return getItemAmount(inv.getContents(), is);
    }

    public static int getItemAmount(ItemStack[] items, ItemStack is){
        boolean checkDurability = true;
        int count = 0;
        for (ItemStack item : items) {
            if (item == null) {
                continue;
            }
            if (item.getType() == is.getType() && ((item.getDurability() == is.getDurability() || item.getDurability() == -1) || !checkDurability) && item.getAmount() > 0) {
                count += item.getAmount();
            }
        }
        return count;
    }

    /// Checks if there is enough free space in inventory
    public static boolean checkFreeSpace(Chest chest,ItemStack is, int left){
        Inventory inv = chest.getInventory();
        return checkFreeSpace(inv, is, left);
    }
   
    public static boolean checkFreeSpace(Inventory inv, ItemStack is, int left){
        return checkFreeSpace(inv.getContents(), is, left);
    }
    public static boolean checkFreeSpace(ItemStack[] contents, ItemStack is, int left){
        Material type = is.getType();
        short durability = is.getDurability();
        int maxStack = is.getType().getMaxStackSize();
        for(ItemStack curitem : contents){
            if(left <= 0){
                return true;
            }
            if(curitem == null){
                left = left - maxStack;
                continue;
            }
            if(curitem.getType() != type || (curitem.getDurability() != durability && curitem.getDurability() != -1)){
                continue;
            }
            int amount = curitem.getAmount();
            if(amount < maxStack){
                left = left - (maxStack - amount);
            }
        }
        return left <= 0;
    }

    public static int amountFreeSpace(ItemStack[] contents, ItemStack is, int left){
    	Material type = is.getType();
        short durability = is.getDurability();
        int maxStack = is.getType().getMaxStackSize();
        for(ItemStack curitem : contents){
            if(curitem == null){
                left = left - maxStack;
                continue;
            }
            if(curitem.getType() != type || (curitem.getDurability() != durability && curitem.getDurability() != -1)){
                continue;
            }
            int amount = curitem.getAmount();
            if(amount < maxStack){
                left = left - (maxStack - amount);
            }
        }
        return -left;
    }

	public static int amountFreeSpace(Chest chest, ItemStack is, int left) {
        Inventory inv = chest.getInventory();
        return amountFreeSpace(inv, is, left);
	}
    //Checks if there is enough free space in inventory
    public static int amountFreeSpace(Inventory inv, ItemStack is, int left){
        return amountFreeSpace(inv.getContents(), is, left);
    }


	@SuppressWarnings("deprecation")
	public static void addItemToInventory(Player player, ItemStack itemStack, int stockAmount) {
		addItemToInventory(player.getInventory(), itemStack,stockAmount);
//		((CraftPlayer)player).getHandle().activeContainer.a();
		player.updateInventory();
	}

    public static void addItemToInventory(Chest chest, ItemStack is, int left){
        addItemToInventory(chest.getInventory(), is, left);
    }

    //Adds item to inventory
    public static void addItemToInventory(Inventory inv, ItemStack is, int left){
        int maxStackSize = is.getType().getMaxStackSize();
        if(left <= maxStackSize){
            is.setAmount(left);
            inv.addItem(is);
            return;
        }

        if(maxStackSize != 64){
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            for (int i = 0; i < Math.ceil(left / maxStackSize); i++) {
                if (left < maxStackSize) {
                    is.setAmount(left);
                    items.add(is);
                    return;
                }else{
                    is.setAmount(maxStackSize);
                    items.add(is);
                }
            }
            Object[] iArray = items.toArray();
            for(Object o : iArray){
                inv.addItem((ItemStack) o);
            }
        }else{
            inv.addItem(is);
        }
    }


    public static int first(Inventory inv, ItemStack is1) {
        if (is1 == null) {
            return -1;
        }
        ItemStack[] inventory = inv.getContents();
        for (int i = 0; i < inventory.length; i++) {
        	ItemStack is2 = inventory[i];
        	if (is2 == null) continue;
            if (is1.getTypeId() == is2.getTypeId() && is1.getDurability() == is2.getDurability()) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * This is nearly a direct copy of the removeItem from CraftBukkit
     * The difference is my ItemStack == ItemStack comparison (found in first())
     * there I change it to go by itemid and datavalue
     * as opposed to itemid and quantity
     * @param inv
     * @param items
     * @return
     */
    public static HashMap<Integer, ItemStack> removeItem(Inventory inv, ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
//            	System.out.println("inv= " + inv + "   " + items.length   + "    item=" + item);
                int first = first(inv, item);
//            	System.out.println("first= " + first);

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                } else {
                    ItemStack itemStack = inv.getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        inv.setItem(first, null);
                    } else {
                        // split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        inv.setItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                // Bail when done
                if (toDelete <= 0) {
                    break;
                }
            }
        }
        return leftover;
    }

    public static String printItemStack(ItemStack is){
    	StringBuilder sb = new StringBuilder("[ItemStack] " +is.getTypeId() + ":" + is.getAmount() + " dura="+is.getDurability());
    	if (is.getData() != null){
    		sb.append(" data=" + is.getData() + "  d.itemType=" + is.getData().getItemType() + " d.itemTypeId=" + is.getData().getItemTypeId() +
    				" d.data=" + is.getData().getData());
    	} else {
    		sb.append(" data=null");
    	}
    	return sb.toString();
    }
    

    //Returns ItemStack 
    // Should change this to work correctly with MaterialData
    // Code from iConomyChestShop, with small fixes for correctly parsing item id with data value
    public static ItemStack getItemStack(String name) {
    	if (name == null || name.isEmpty())
    		return null;
    	name = name.replace(" ", "_");
    	name = name.replace(":", ";");
    	name = Util.deColorChat(name);

        int dataPosition = name.indexOf(';');
        dataPosition = (dataPosition != -1 ? dataPosition : -1);
        int dataValue = -1;
        if (dataPosition != -1){
        	dataValue = (Util.isInt(name.substring(dataPosition + 1)) ? Integer.parseInt(name.substring(dataPosition + 1)) : 0);
        } 
   
        dataValue = (dataValue > 1000 || dataValue < 0 ? 0 : dataValue);
        Material mat;
        if(dataPosition != -1){
            mat = Util.getMat(name.substring(0,dataPosition));
        }else{
            mat = Util.getMat(name);
        }
        if (mat != null && mat != Material.AIR) {
            return new ItemStack(mat, 0, (short) dataValue);
        }
        return null;
    }
//    public static String camelCase(String str){
//    	StringBuilder sb = new StringBuilder();
//    	boolean first = true;
//    	for (String s : str.split("_")){
//    		sb.append(arg0)
//    		first = false;
//    	}
//    	str.substring(0, 1).toUpperCase() + str.substring(1);
//    }
    
}
