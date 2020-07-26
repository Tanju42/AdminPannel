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

public class GamemodeInventory implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}

    private static SmartInventory GamemodeInv = SmartInventory.builder().
            id("GamemodeInv")
            .provider(new GamemodeInventory())
            .title(ChatColor.GOLD+"GameMode")
            .size(2,9)
            .build();

    public static void open(Player p) {
        GamemodeInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        //##################################################
        ItemStack Background = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta BackgroundMeta = Background.getItemMeta();
        BackgroundMeta.setDisplayName(" ");
        Background.setItemMeta(BackgroundMeta);
        ItemStack Nix = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        contents.fillRow(0, ClickableItem.empty(Background));
        contents.fillRow(1, ClickableItem.empty(Nix));
        contents.set(0, 2, Types("survival", 9907902));
        contents.set(0, 3, Types("adventure", 9907903));
        contents.set(0, 5, Types("creative", 9907904));
        contents.set(0, 6, Types("spectator", 9907905));
        contents.set(1, 2, World(p));
        contents.set(1, 4, Back());

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
                GamemodeInv.close(p);
            }
        });
        return ClickItem;
    }

    public ClickableItem Types(String Art, Integer CostumModelData) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+Art);
        itemMeta.setCustomModelData(CostumModelData);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um dein GameMode auf");
        lore.add(ChatColor.BLUE+Art+ChatColor.GRAY+" zu stellen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode "+Art+" "+p.getName());
            p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"GameMode wurde auf "+ChatColor.GREEN+Art+ChatColor.YELLOW+" gestellt!");
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
}
