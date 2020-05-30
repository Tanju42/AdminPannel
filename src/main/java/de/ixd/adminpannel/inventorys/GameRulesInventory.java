package de.ixd.adminpannel.inventorys;

import com.sun.org.glassfish.gmbal.Description;
import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class GameRulesInventory implements InventoryProvider {

    private static HashMap<GameRule, ArrayList<String>> Descriptions = new HashMap<GameRule, ArrayList<String>>();
    private static SmartInventory GameRulesInv = SmartInventory.builder().
            id("GameRulesInv")
            .provider(new GameRulesInventory())
            .title(ChatColor.GOLD+"GameRules")
            .size(5,9)
            .build();

    public static void open(Player p) {
        fillDescriptionHash();
        GameRulesInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);

        for ( GameRule rule : GameRule.values()) {
            contents.add(Arten(p, rule, getDescription(rule)));
        }
        contents.fillRow(4, ClickableItem.empty(Nix));
        contents.set(4, 4, Back());
        contents.set(4, 2, World(p));
        contents.set(4, 6, Reset(p));

    }

    public void update(Player p, InventoryContents contents) {

    }

    public ClickableItem Back() {
        ItemStack item = new ItemStack(Material.IRON_DOOR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE+"Zurück");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um Zurück");
        lore.add(ChatColor.GRAY+"zu kommen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            MainInventory.open(p);
        });
        return ClickItem;
    }

    public ClickableItem World(Player p) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Momentane Welt");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+p.getWorld().getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.empty(item);
        return ClickItem;
    }

    public ClickableItem Reset(Player p) {
        ItemStack item = new ItemStack(Material.PURPLE_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"Reset");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um alles auf");
        lore.add(ChatColor.GRAY+"die standard Werte");
        lore.add(ChatColor.GRAY+"zu stellen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            for (String ruleName : p.getWorld().getGameRules() ) {
                    GameRule rule = GameRule.getByName(ruleName);
                    p.getWorld().setGameRule(rule, p.getWorld().getGameRuleDefault(rule));
            }
            p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Alle GameRules wurden "+ChatColor.LIGHT_PURPLE+"Resetet"+ChatColor.YELLOW+"!");
            open(p);
        });
        return ClickItem;
    }

    public ClickableItem Arten(Player p, GameRule Art, ArrayList<String> Description) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.BLUE+Art.getName());
        //Test if GameRule ist Boolean
        if (Art.getType().toString().contains("Boolean")) { // Boolean
            if ((boolean) p.getWorld().getGameRuleValue(Art)) {
                item = new ItemStack(Material.LIME_DYE);
                lore.add(ChatColor.YELLOW+"Current"+ChatColor.DARK_GRAY+": "+ChatColor.GREEN+"true");
            } else {
                item = new ItemStack(Material.GRAY_DYE);
                lore.add(ChatColor.YELLOW+"Current"+ChatColor.DARK_GRAY+": "+ChatColor.RED+"false");
            }
        } else { // Integer
            lore.add(ChatColor.YELLOW+"Current"+ChatColor.DARK_GRAY+": "+ChatColor.GOLD+p.getWorld().getGameRuleValue(Art));
        }
        lore.add(ChatColor.YELLOW+"Description"+ChatColor.DARK_GRAY+":");
        for (String text : Description) {
            lore.add(ChatColor.GRAY+text);
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (Art.getType().toString().contains("Boolean")) { // Boolean
                if ((boolean) p.getWorld().getGameRuleValue(Art)) { // true
                    p.getWorld().setGameRule(Art, false);
                    p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"GameRule "+ChatColor.BLUE+Art.getName()+ChatColor.YELLOW+" wurde auf "+ChatColor.RED+"false "+ChatColor.YELLOW+"gestellt!");
                } else { // false
                    p.getWorld().setGameRule(Art, true);
                    p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"GameRule "+ChatColor.BLUE+Art.getName()+ChatColor.YELLOW+" wurde auf "+ChatColor.GREEN+"true "+ChatColor.YELLOW+"gestellt!");
                }
                open(p);
            } else { // Integer
                new AnvilGUI.Builder()
                        .onClose(player -> {
                            open(p);
                        })
                        .onComplete((player, text) -> {
                            Integer value = 1;
                            try {
                                value = Integer.parseInt(text);
                                player.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"GameRule "+ChatColor.BLUE+Art.getName()+ChatColor.YELLOW+" wurde auf "+ChatColor.BLUE+text+ChatColor.YELLOW+" gestellt!");
                                p.getWorld().setGameRule(Art, value);
                                return AnvilGUI.Response.close();
                            } catch(Exception eX) {
                                player.sendMessage(AdminPannel.prefix+ChatColor.RED+"Nur ein Integer ist erlaubt!");
                                return AnvilGUI.Response.text(String.valueOf(p.getWorld().getGameRuleValue(Art)));
                            }
                        })
                        .text(String.valueOf(p.getWorld().getGameRuleValue(Art)))
                        .item(new ItemStack(Material.PAPER))
                        .title(ChatColor.BLUE+Art.getName())
                        .plugin(AdminPannel.plugin)
                        .open(p);
            }
        });
        return ClickItem;
    }

    private static ArrayList<String> getDescription(GameRule rule) {
        if (!Descriptions.containsKey(rule)) {
            ArrayList<String> NotFound = new ArrayList<>();
            NotFound.add(ChatColor.RED+"Error! Not found in language file!");
            return NotFound;
        } else {
            return Descriptions.get(rule);
        }
    }



    private static void fillDescriptionHash() {
        FileConfiguration language = AdminPannel.getLanguage();
        if (!Descriptions.isEmpty()) {
            Descriptions.clear();
        }
        for ( String rule : language.getConfigurationSection("inventory.gamerules.description").getKeys(false)) {
            Descriptions.put(GameRule.getByName(rule), (ArrayList<String>) language.getList("inventory.gamerules.description."+rule));
        }
        AdminPannel.plugin.getLogger().info("Descriptions: "+Descriptions.toString());
        AdminPannel.plugin.getLogger().info("Keys: "+language.getConfigurationSection("inventory.gamerules.description").getKeys(false));
    }
}
