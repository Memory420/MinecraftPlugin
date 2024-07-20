package org.example;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new JoinNotification(this), this);
        getCommand("kits").setExecutor(new KitCommand(this));
        getServer().getPluginManager().registerEvents(new KitCommand(this), this);
    }
}