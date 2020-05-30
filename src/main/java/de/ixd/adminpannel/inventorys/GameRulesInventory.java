package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GameRulesInventory implements InventoryProvider {

    private static SmartInventory GameRulesInv = SmartInventory.builder().
            id("GameRulesInv")
            .provider(new GameRulesInventory())
            .title(ChatColor.GOLD+"GameRules")
            .size(4,9)
            .build();

    public static void open(Player p) {
        GameRulesInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName("");
        Nix.setItemMeta(NixMeta);
        for ( GameRule rule : GameRule.values()) {
            contents.add(Arten(p, rule));
        }
        contents.set(3, 8, Back());

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

    public ClickableItem Arten(Player p, GameRule Art) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.BLUE+Art.getName());
        lore.add(ChatColor.GRAY+"Klicken um das GameRule zu ändern!");
        //Test if GameRule ist Boolean
        if (Art.getType().toString().contains("Boolean")) { // Boolean
            if ((boolean) p.getWorld().getGameRuleValue(Art)) {
                item = new ItemStack(Material.LIME_DYE);
                lore.add(ChatColor.GRAY+"Current: "+ChatColor.GREEN+"true");
            } else {
                item = new ItemStack(Material.GRAY_DYE);
                lore.add(ChatColor.GRAY+"Current: "+ChatColor.RED+"false");
            }
        } else { // Integer
            lore.add(ChatColor.GRAY+"Current: "+ChatColor.GOLD+p.getWorld().getGameRuleValue(Art));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (Art.getType().toString().contains("Boolean")) { // Boolean

            } else { // Integer

            }
        });
        return ClickItem;
    }
}
