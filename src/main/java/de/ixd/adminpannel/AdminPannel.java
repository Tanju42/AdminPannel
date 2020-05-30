package de.ixd.adminpannel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;


public class AdminPannel extends JavaPlugin {

    public static AdminPannel plugin;
    public static String prefix = ChatColor.RED+"Admin"+ChatColor.BLUE+"Tools"+ChatColor.GRAY+" | "+ChatColor.RESET;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();
        plugin.getCommand("admingui").setExecutor(new Commands());
        getLogger().info(ChatColor.GREEN+"Plugin Enabled!");
        getLogger().info(ChatColor.AQUA+"Daylight_Cycle: "+Bukkit.getServer().getWorld("world").getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)+" Type: "+GameRule.RANDOM_TICK_SPEED.getType().toString().contains("Integer"));
        Bukkit.getServer().getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        getLogger().info(ChatColor.AQUA+"Daylight_Cycle: "+Bukkit.getServer().getWorld("world").getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)+" Type: "+GameRule.DO_DAYLIGHT_CYCLE.getType().toString().contains("Integer"));
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED+"Plugin Disabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"Ich bin die Console");
    }
}
