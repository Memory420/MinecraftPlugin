package org.example;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinNotification implements Listener {
    private Main plugin;

    public JoinNotification(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoined(PlayerJoinEvent event){
        plugin.getServer().broadcastMessage( ChatColor.AQUA + event.getPlayer().getDisplayName() + " зашёл на сервер");
        event.getPlayer().sendMessage("Версия сборки: " + plugin.getDescription().getVersion());
    }
}
