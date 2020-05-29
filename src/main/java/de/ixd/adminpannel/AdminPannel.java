package de.ixd.adminpannel;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class AdminPannel extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN+"Plugin Enabled!");


    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED+"Plugin Disabled!");
    }
}
