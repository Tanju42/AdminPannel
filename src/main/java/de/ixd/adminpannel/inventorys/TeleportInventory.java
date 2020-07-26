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

public class TeleportInventory implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}

    private static SmartInventory TeleportInv = SmartInventory.builder().
            id("TeleportInv")
            .provider(new TeleportInventory())
            .title(ChatColor.GOLD+"Teleport")
            .size(2,9)
            .build();

    public static void open(Player p) {
        TeleportInv.open(p);
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
        contents.set(0, 2, Types("Spieler -> Ich", 9911901));
        contents.set(0, 3, Types("Alle -> Ich", 9911902));
        contents.set(0, 5, Types("Ich -> Spieler", 9911903));
        contents.set(0, 6, Types("Alle -> Spieler", 9911904));
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
                TeleportInv.close(p);
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
        lore.add(ChatColor.GRAY+"Klicken um "+ChatColor.BLUE+Art);
        lore.add(ChatColor.GRAY+" zu Teleportieren!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            p.sendMessage(ChatColor.DARK_RED+"Nicht implementiert!");
        });
        return ClickItem;
    }
}
