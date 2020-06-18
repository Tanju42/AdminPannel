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
            .size(2,9)
            .build();

    public static void open(Player p) {
        MainInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        contents.add(Wetter());
        contents.add(GameRule());
        contents.add(Sounds());
        contents.add(Whitelist());
        contents.add(Effects());
    }

    public ClickableItem Wetter() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Wetter");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um das Wetter");
        lore.add(ChatColor.GRAY+"zu ändern!");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"- Wetter Clear");
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
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"GameRule");
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
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Whitelist");
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
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Sounds");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um die Sounds");
        lore.add(ChatColor.GRAY+"zu hören!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            SoundsInventory.open(p, 0);
        });
        return ClickItem;
    }

    public ClickableItem Effects() {
        ItemStack item = new ItemStack(Material.BOOK);
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

    public ClickableItem template() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("DisplayName");
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
