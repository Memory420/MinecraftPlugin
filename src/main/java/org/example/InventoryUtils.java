package org.example;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtils {
    public static int getFreeSlots(Player player){
        PlayerInventory inv = player.getInventory();
        int freeSlots = 0;

        ItemStack[] contents  = inv.getStorageContents();
        for (ItemStack item : contents){
            if (item == null || item.getType() == org.bukkit.Material.AIR){
                freeSlots++;
            }
        }
        return freeSlots;
    }
}
