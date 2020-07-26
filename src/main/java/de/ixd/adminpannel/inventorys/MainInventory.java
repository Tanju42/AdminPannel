package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MainInventory implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}

    private static SmartInventory MainInv = SmartInventory.builder()
            .id("MainInv")
            .manager(AdminPannel.SmartInvsMan)
            .provider(new MainInventory())
            .title(ChatColor.RED+"Admin Pannel")
            .size(6,9)
            .build();

    public static void open(Player p) {
        MainInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        //##################################################
        ItemStack Background = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta BackgroundMeta = Background.getItemMeta();
        BackgroundMeta.setDisplayName(" ");
        Background.setItemMeta(BackgroundMeta);
        //##################################################
        ItemStack Nix = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        contents.fill(ClickableItem.empty(Background));
        contents.set(1, 1, ban());
        contents.set(1, 2, Whitelist());
        contents.set(1, 3, kick());
        contents.set(1, 5, rank());
        contents.set(1, 6, GameRule());
        contents.set(1, 7, gamemode());
        contents.set(3, 2, teleport());
        contents.set(3, 4, Weather());
        contents.set(3, 6, Effects());
        contents.set(4, 2, warps());
        contents.set(4, 4, time());
        contents.set(4, 6, Sounds());
        contents.fillRow(MainInv.getRows()-1, ClickableItem.empty(Nix));
        contents.set(MainInv.getRows()-1, 4, Close());
    }

    public ClickableItem Close() {
        ItemStack item = new ItemStack(Material.IRON_DOOR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE+"Schließen");
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
        });
        return ClickItem;
    }

    public ClickableItem Weather() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Wetter");
        itemMeta.setCustomModelData(9902901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um das Wetter");
        lore.add(ChatColor.GRAY+"zu ändern!");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"- clear");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                WeatherInventory.open(p);
            } else if (e.isRightClick()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
                p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Wetter wurde auf "+ChatColor.GREEN+"clear"+ChatColor.YELLOW+" gestellt!");
                p.closeInventory();
            }
        });
        return ClickItem;
    }

    public ClickableItem GameRule() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"GameRule");
        itemMeta.setCustomModelData(9904901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um die GameRules");
        lore.add(ChatColor.GRAY+"zu ändern!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            GameRulesInventory.open(p);
        });
        return ClickItem;
    }

    public ClickableItem Whitelist() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Whitelist");
        itemMeta.setCustomModelData(9905901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um die Whitelist");
        lore.add(ChatColor.GRAY+"zu bearbeiten!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            WhitelistInventory.open(p, 0);
        });
        return ClickItem;
    }

    public ClickableItem Sounds() {
        ItemStack item = new ItemStack(Material.JUKEBOX);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Sounds");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um die Sounds");
        lore.add(ChatColor.GRAY+"zu hören!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            SoundsInventory.open(p, 0, 1, 1);
        });
        return ClickItem;
    }

    public ClickableItem Effects() {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Effects");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um dir Effects");
        lore.add(ChatColor.GRAY+"zu geben!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            EffectsInventory.open(p);
        });
        return ClickItem;
    }

    public ClickableItem ban() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Ban");
        itemMeta.setCustomModelData(9906901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um zum Bannen");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            BanInventory.open(p, 0);
        });
        return ClickItem;
    }

    public ClickableItem gamemode() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"GameMode");
        itemMeta.setCustomModelData(9907901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um dein GameMode");
        lore.add(ChatColor.GRAY+"zu ändern!");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"- creative");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                GamemodeInventory.open(p);
            } else if (e.isRightClick()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode creative "+p.getName());
                p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"GameMode wurde auf "+ChatColor.GREEN+"creative"+ChatColor.YELLOW+" gestellt!");
                p.closeInventory();
            }
        });
        return ClickItem;
    }

    public ClickableItem kick() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Kick");
        itemMeta.setCustomModelData(9908901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um zum Kicken");
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Mit Kopf");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Mit Name");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                KickInventory.openHead(p, 0);
            } else if (e.isRightClick()) {
                KickInventory.openName(p);
            }
        });
        return ClickItem;
    }

    public ClickableItem rank() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Rank");
        itemMeta.setCustomModelData(9909901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um den Rank");
        lore.add(ChatColor.GRAY+"zu ändern!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                p.sendMessage("Links");
            } else if (e.isRightClick()) {
                p.sendMessage("Rechts");
            }
        });
        return ClickItem;
    }

    public ClickableItem teleport() {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Teleport");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um zum Teleportieren");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            TeleportInventory.open(p);
        });
        return ClickItem;
    }

    public ClickableItem time() {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Zeit");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um die Zeit");
        lore.add(ChatColor.GRAY+"zu ändern!");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"- day");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                TimeInventory.open(p);
            } else if (e.isRightClick()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set day");
                p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Zeit wurde auf "+ChatColor.GREEN+"day"+ChatColor.YELLOW+" gestellt!");
                p.closeInventory();
            }
        });
        return ClickItem;
    }

    public ClickableItem warps() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Warps");
        itemMeta.setCustomModelData(9912901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um zum Warpen");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                p.sendMessage("Links");
            } else if (e.isRightClick()) {
                p.sendMessage("Rechts");
            }
        });
        return ClickItem;
    }

    public ClickableItem template() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"DisplayName");
        itemMeta.setCustomModelData(9902901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("lore1");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                p.sendMessage("Links");
            } else if (e.isRightClick()) {
                p.sendMessage("Rechts");
            }
        });
        return ClickItem;
    }
}
