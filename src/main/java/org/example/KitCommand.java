package org.example;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

import static org.example.InventoryUtils.getFreeSlots;

public class KitCommand implements CommandExecutor, Listener {
    private final HashMap<UUID, Long> cooldown_kit1;
    private final HashMap<UUID, Long> cooldown_kit2;
    private final NamespacedKey KIT_KEY = new NamespacedKey("testplugin", "kit_type");
    private final Main plugin;

    public KitCommand(Main plugin) {
        this.cooldown_kit1 = new HashMap<>();
        this.cooldown_kit2 = new HashMap<>();
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage("Команды доступны только игрокам");
            return true;
        }
        if (command.getName().equalsIgnoreCase("kits")){
            Player player = ((Player) commandSender).getPlayer();
            player.openInventory(KitMenu(CreateInventory(9, "Kit menu")));
        }
        return true;
    }
    private Inventory CreateInventory(int slotNumber, String invName){
        Inventory inventory = Bukkit.createInventory(null, slotNumber, invName);
        return inventory;
    }
    private Inventory KitMenu(Inventory inventory){
        ItemStack item1 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack item2 = new ItemStack(Material.GOLDEN_APPLE, 1);

        ItemMeta itemMeta1 = item1.getItemMeta();
        itemMeta1.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta1.setDisplayName(ChatColor.GREEN + "Кит кожанки");
        itemMeta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta1.getPersistentDataContainer().set(KIT_KEY, PersistentDataType.STRING, "leather_set");
        item1.setItemMeta(itemMeta1);

        ItemMeta itemMeta2 = item2.getItemMeta();
        itemMeta2.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta2.setDisplayName(ChatColor.GREEN + "Кит хавки");
        itemMeta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta2.getPersistentDataContainer().set(KIT_KEY, PersistentDataType.STRING, "food_set");
        item2.setItemMeta(itemMeta2);

        inventory.setItem(0, item1);
        inventory.setItem(1, item2);
        return inventory;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getView().getTitle().equals("Kit menu")) {
            ItemStack item = event.getCurrentItem();
            if (item != null && item.hasItemMeta()) {
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta != null && itemMeta.getPersistentDataContainer().has(KIT_KEY, PersistentDataType.STRING)) {
                    String kitType = itemMeta.getPersistentDataContainer().get(KIT_KEY, PersistentDataType.STRING);
                    Player player = (Player) event.getWhoClicked();

                    if (kitType.equals("leather_set")) {
                        if (!this.cooldown_kit1.containsKey(player.getUniqueId())) {
                            this.cooldown_kit1.put(player.getUniqueId(), System.currentTimeMillis());
                            player.sendMessage(ChatColor.DARK_AQUA + "Взят набор кожанки");
                            if (getFreeSlots(player) >= 4){
                                givePlayerKit1(player);
                            }
                        } else {
                            long timeElapsed = System.currentTimeMillis() - cooldown_kit1.get(player.getUniqueId());

                            if (timeElapsed >= 5000) {
                                this.cooldown_kit1.put(player.getUniqueId(), System.currentTimeMillis());
                                player.sendMessage(ChatColor.DARK_AQUA + "Взят набор кожанки");
                                if (getFreeSlots(player) >= 4){
                                    givePlayerKit1(player);
                                }
                            } else {
                                player.sendMessage(ChatColor.DARK_RED + "Набор кожанки перезарядится через " + (int) (5000 - timeElapsed) / 1000);
                            }
                        }
                    } else if (kitType.equals("food_set")) {
                        if (!this.cooldown_kit2.containsKey(player.getUniqueId())) {
                            this.cooldown_kit2.put(player.getUniqueId(), System.currentTimeMillis());
                            player.sendMessage(ChatColor.DARK_AQUA + "Взят набор хавчика");
                            if (getFreeSlots(player) >= 4){
                                givePlayerKit2(player);
                            }
                        } else {
                            long timeElapsed = System.currentTimeMillis() - cooldown_kit2.get(player.getUniqueId());

                            if (timeElapsed >= 5000) {
                                this.cooldown_kit2.put(player.getUniqueId(), System.currentTimeMillis());
                                player.sendMessage(ChatColor.DARK_AQUA + "Взят набор хавчика");
                                if (getFreeSlots(player) >= 4){
                                    givePlayerKit2(player);
                                }
                            } else {
                                player.sendMessage(ChatColor.DARK_RED + "Набор хавчика перезарядится через " + (int) (5000 - timeElapsed) / 1000);
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
    public void givePlayerKit1(Player player){
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        ItemStack stone_sword = new ItemStack(Material.STONE_SWORD, 1);

        player.getInventory().addItem(helmet);
        player.getInventory().addItem(chestplate);
        player.getInventory().addItem(leggings);
        player.getInventory().addItem(boots);
        player.getInventory().addItem(stone_sword);

        player.sendMessage(ChatColor.DARK_AQUA + "Выдан набор кожанщика");
    }
    public void givePlayerKit2(Player player){
        ItemStack food1 = new ItemStack(Material.COOKED_CHICKEN, 8);
        ItemStack food2 = new ItemStack(Material.COOKED_BEEF, 8);
        ItemStack food3 = new ItemStack(Material.COOKED_MUTTON, 8);
        ItemStack food4 = new ItemStack(Material.APPLE, 8);

        player.getInventory().addItem(food1);
        player.getInventory().addItem(food2);
        player.getInventory().addItem(food3);
        player.getInventory().addItem(food4);

        player.sendMessage(ChatColor.DARK_AQUA + "Выдан набор хавчика");
    }
}
