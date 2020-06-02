package de.ixd.adminpannel;

import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;


public class AdminPannel extends JavaPlugin {
    public static InventoryManager SmartInvsMan;

    private static ArrayList<String> configs = new ArrayList<String>();
    public static AdminPannel plugin;
    public static String prefix = ChatColor.RED+"Admin"+ChatColor.BLUE+"Tools"+ChatColor.GRAY+" | "+ChatColor.RESET;

    @Override
    public void onEnable() {
        plugin = this;
        SmartInvsMan = new InventoryManager(plugin);
        SmartInvsMan.init();
        plugin.saveDefaultConfig();
        plugin.getCommand("admingui").setExecutor(new Commands());
        plugin.getCommand("admingui").setTabCompleter(new CommandTabComplete());
        createLanguageFiles();
        loadConfigs();
        getLogger().info(ChatColor.GREEN+"Plugin Enabled!");
        getLogger().info(ChatColor.AQUA+"Daylight_Cycle: "+Bukkit.getServer().getWorld("world").getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)+" Type: "+GameRule.RANDOM_TICK_SPEED.getType().toString().contains("Integer"));
        Bukkit.getServer().getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        getLogger().info(ChatColor.AQUA+"Daylight_Cycle: "+Bukkit.getServer().getWorld("world").getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)+" Type: "+GameRule.DO_DAYLIGHT_CYCLE.getType().toString().contains("Integer"));
    }

    private void loadConfigs() {

    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED+"Plugin Disabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA+"Ich bin die Console");
    }


    private void createLanguageFiles() {
        ArrayList<String> languages = new ArrayList<String>();
        languages.add("language_EN.yml");
        configs.addAll(languages);
        for (String name : languages) {
            File language = new File(plugin.getDataFolder()+"/languages/"+name);
            if (!language.exists()) {
                language.mkdirs();
                copy(name, getClass().getResourceAsStream("/languages/"+name), plugin.getDataFolder()+"/languages/"+name);
            }
        }

    }

    public static void copy(String name, InputStream source , String destination) {
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, prefix+ChatColor.RED+"Error by copying languages!", ex);
        }
        plugin.getLogger().info(prefix+ChatColor.YELLOW+"Language "+name+" created!");
    }

    public static FileConfiguration getLanguage() {
        FileConfiguration FCfg = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder()+"/languages/"+configs.get(0)));
        return FCfg;
    }
}
