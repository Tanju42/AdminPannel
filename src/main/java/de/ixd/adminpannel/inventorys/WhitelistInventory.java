package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotPos;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Set;

public class WhitelistInventory  implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}


    private static Pagination pagination = null;
    private static Integer rows = 2;

    private static SmartInventory WhitelistInv = SmartInventory.builder().
            id("WhitelistInv")
            .provider(new WhitelistInventory())
            .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"|"+ChatColor.AQUA+" 0")
            .size(rows,9)
            .build();

    public static void open(Player p, Integer Page) {
        for (int tempRows = 9; tempRows <= Bukkit.getOnlinePlayers().size(); tempRows = tempRows + 9) {
            rows++;
            if (tempRows == 54) {
                break;
            }
        }
        Integer TempPage = 0;
        if (pagination != null) {
            TempPage = pagination.getPage();
        }
        WhitelistInv = SmartInventory.builder().
                id("WhitelistInv")
                .provider(new WhitelistInventory())
                .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"| "+ChatColor.AQUA+TempPage)
                .size(rows,9)
                .build();
        WhitelistInv.open(p, Page);
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        pagination = contents.pagination();
        contents.fillRow(rows-1, ClickableItem.empty(Nix));
        contents.set(rows-1, 4, Back());


        //##################################################

        ArrayList<OfflinePlayer> PlayersOnPage = new ArrayList<>();

        for (OfflinePlayer Op : Bukkit.getWhitelistedPlayers()) {
            contents.add(TempSkullRemove(Op));
            PlayersOnPage.add(Op);
        }

        UpdateSkins(PlayersOnPage, contents, p);

        //##################################################

    }

    private void UpdateSkins(ArrayList<OfflinePlayer> Op, InventoryContents contents, Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(AdminPannel.plugin, new Runnable() {
            @Override
            public void run() {
                Integer row = 0;
                Integer column = 0;
                for (OfflinePlayer OpTemp : Op) {
                    contents.set(row, column, SkullRemove(OpTemp));
                    System.out.println("ADDED: "+OpTemp.getName()+" R:"+row+" C:"+column);
                    if (column == 8) {
                        column = 0;
                        row++;
                    }
                    column++;
                    p.updateInventory();
                }
            }
        });
    }

    private ClickableItem Back() {
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

    private ClickableItem SkullAdd(Player p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Hinzufügen");
        ClickableItem ClickItem = ClickableItem.of(getSkull(p, ChatColor.GREEN+p.getName(), lore), e -> {
            //ADD
        });
        return ClickItem;
    }

    private ClickableItem TempSkullAdd(Player p) {
        ItemStack item = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED+p.getName());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Hinzufügen");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            //ADD
        });
        return ClickItem;
    }

    private ClickableItem SkullRemove(OfflinePlayer p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Entfernen");
        ClickableItem ClickItem = ClickableItem.of(getSkullOffline(p, ChatColor.RED+p.getName(), lore), e -> {
            //Remove
        });
        return ClickItem;
    }

    private ClickableItem TempSkullRemove(OfflinePlayer p) {
        ItemStack item = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED+p.getName());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Entfernen");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            //Remove
        });
        return ClickItem;
    }

    private static ItemStack getSkullOffline(OfflinePlayer p, String ItemName, ArrayList<String> lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(p);
        skullMeta.setDisplayName(ItemName);
        skullMeta.setLore(lore);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    private static ItemStack getSkull(Player p, String ItemName, ArrayList<String> lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(p);
        skullMeta.setDisplayName(ItemName);
        skullMeta.setLore(lore);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }


}
