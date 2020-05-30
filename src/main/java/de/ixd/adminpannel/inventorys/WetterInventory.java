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

public class WetterInventory implements InventoryProvider {

    private static SmartInventory WetterInv = SmartInventory.builder().
            id("WetterInv")
            .provider(new WetterInventory())
            .title(ChatColor.GOLD+"Wetter")
            .size(1,9)
            .build();

    public static void open(Player p) {
        WetterInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName("");
        Nix.setItemMeta(NixMeta);
        contents.fill(ClickableItem.empty(Nix));
        contents.set(0, 3, Arten("clear"));
        contents.set(0, 4, Arten("rain"));
        contents.set(0, 5, Arten("thunder"));
        contents.set(0, 8, Back());

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

    public ClickableItem Arten(String Art) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+Art);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um das Wetter auf");
        lore.add(ChatColor.GREEN+Art+ChatColor.GRAY+" zu stellen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather "+Art);
            Player p = (Player) e.getWhoClicked();
            p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Wetter wurde auf "+ChatColor.GREEN+Art+ChatColor.YELLOW+" gestellt!");
            p.closeInventory();
        });
        return ClickItem;
    }
}
