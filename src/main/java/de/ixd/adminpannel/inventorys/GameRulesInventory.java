package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
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
    public void update(Player p, InventoryContents contents) {}

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
        ItemStack Background = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta BackgroundMeta = Background.getItemMeta();
        BackgroundMeta.setDisplayName(" ");
        Background.setItemMeta(BackgroundMeta);
        //##################################################
        ItemStack Nix = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);

        contents.set(3, 0, ClickableItem.empty(Background));
        contents.set(3, 1, ClickableItem.empty(Background));
        contents.set(3, 4, ClickableItem.empty(Background));
        contents.set(3, 7, ClickableItem.empty(Background));
        contents.set(3, 8, ClickableItem.empty(Background));
        for ( GameRule rule : GameRule.values()) {
            contents.add(Arten(p, rule, getDescription(rule)));
        }
        contents.fillRow(4, ClickableItem.empty(Nix));
        contents.set(4, 4, Back());
        contents.set(4, 2, World(p));
        contents.set(4, 6, Reset(p));

    }

    public ClickableItem Back() {
        ItemStack item = new ItemStack(Material.IRON_DOOR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE+"Zurück");
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                MainInventory.open(p);
            } else if (e.isRightClick()) {
                GameRulesInv.close(p);
            }
        });
        return ClickItem;
    }

    public ClickableItem World(Player p) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Momentane Welt");
        itemMeta.setCustomModelData(9901801);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+p.getWorld().getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.empty(item);
        return ClickItem;
    }

    public ClickableItem Reset(Player p) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"Reset");
        itemMeta.setCustomModelData(9901809);
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
                GameRulesInv.close(p);
                new AnvilGUI.Builder()
                        .onClose(player -> {
                            Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                                GameRulesInv.open(player);
                            });
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
    }
}
