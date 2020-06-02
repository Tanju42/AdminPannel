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

public class WeatherInventory implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}

    private static SmartInventory WeatherInv = SmartInventory.builder().
            id("WetterInv")
            .provider(new WeatherInventory())
            .title(ChatColor.GOLD+"Wetter")
            .size(2,9)
            .build();

    public static void open(Player p) {
        WeatherInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        contents.fill(ClickableItem.empty(Nix));
        contents.set(0, 3, Types("clear"));
        contents.set(0, 4, Types("rain"));
        contents.set(0, 5, Types("thunder"));
        contents.set(1, 2, World(p));
        contents.set(1, 4, Back());

    }

    public ClickableItem Back() {
        ItemStack item = new ItemStack(Material.IRON_DOOR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE+"Zurück");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Zurück");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Schließen");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                MainInventory.open(p);
            } else if (e.isRightClick()) {
                WeatherInv.close(p);
            }
        });
        return ClickItem;
    }

    public ClickableItem Types(String Art) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+Art);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um das Wetter auf");
        lore.add(ChatColor.BLUE+Art+ChatColor.GRAY+" zu stellen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather "+Art);
            p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Wetter wurde auf "+ChatColor.GREEN+Art+ChatColor.YELLOW+" gestellt!");
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
}
