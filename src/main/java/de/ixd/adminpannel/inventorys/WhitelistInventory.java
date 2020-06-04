package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class WhitelistInventory  implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}


    private static Pagination pagination = null;
    private static Integer rows = 2;
    private static Integer TempPage = 0;

    private static SmartInventory WhitelistInv = SmartInventory.builder().
            id("WhitelistInv")
            .provider(new WhitelistInventory())
            .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"|"+ChatColor.AQUA+" 0")
            .size(rows,9)
            .build();

    public static void open(Player p, Integer Page) {
        rows = 2;
        System.out.println("HIER  asd");
        for (int tempRows = 9; tempRows < Bukkit.getWhitelistedPlayers().size(); tempRows = tempRows + 9) {
            System.out.println("HIER "+tempRows);
            rows++;
            if (tempRows == 54) {
                break;
            }
        }
        if (pagination != null) {
            while(Page != TempPage) {
                if (Page > TempPage) {
                    pagination.next();
                } else {
                    pagination.previous();
                }
                TempPage = pagination.getPage();
            }
            TempPage = pagination.getPage();
        }
        if (TempPage == 0 && rows > 6) {
            rows = 6;
        }
        if (TempPage > 0) {
            for (int i = TempPage; i > 0; i--) {
                rows = rows - 5;
            }
        }
        WhitelistInv = SmartInventory.builder().
                id("WhitelistInv")
                .provider(new WhitelistInventory())
                .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"| "+ChatColor.AQUA+TempPage)
                .size(rows,9)
                .build();
        WhitelistInv.open(p, TempPage);
    }

    public void init(Player p, InventoryContents contents) {
        if (pagination == null) {
            pagination = contents.pagination();
        }
        Integer currPage = pagination.getPage();
        //##################################################
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        ItemStack Previous = new ItemStack(Material.ARROW);
        ItemMeta PreviousMeta = Nix.getItemMeta();
        PreviousMeta.setDisplayName(ChatColor.DARK_AQUA+"Previous");
        ArrayList<String> Backlore = new ArrayList<>();
        Backlore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.DARK_AQUA+"First");
        PreviousMeta.setLore(Backlore);
        Previous.setItemMeta(PreviousMeta);
        //##################################################
        ItemStack Next = new ItemStack(Material.ARROW);
        ItemMeta NextMeta = Nix.getItemMeta();
        NextMeta.setDisplayName(ChatColor.DARK_AQUA+"Next");
        ArrayList<String> Nextlore = new ArrayList<>();
        Nextlore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.DARK_AQUA+"Last");
        NextMeta.setLore(Nextlore);
        Next.setItemMeta(NextMeta);
        //##################################################
        HashMap<String, OfflinePlayer> WhitelistedHashMap = new HashMap<>();
        // <Name, OfflinePlayer>
        for (OfflinePlayer Op : Bukkit.getWhitelistedPlayers()) {
            WhitelistedHashMap.put(Op.getName(), Op);
        }
        ArrayList<String> WhitelistedPlayers = new ArrayList<>(WhitelistedHashMap.keySet());


        ClickableItem[] items = new ClickableItem[WhitelistedPlayers.size()];

        for(int i = 0; i < items.length; i++) {
            items[i] = TempSkullRemove(WhitelistedHashMap.get(WhitelistedPlayers.get(i)));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(45);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        ArrayList<OfflinePlayer> PlayersOnPage = new ArrayList<>();
        Integer from = (pagination.getPage() * 45);
        Integer to = ((pagination.getPage()+1) * 45);
        if (WhitelistedPlayers.size() < to) {
            to = WhitelistedPlayers.size();
        }
        List<String> PlayerNamesOnPage = WhitelistedPlayers.subList(from, to);
        for (String Name : PlayerNamesOnPage) {
            PlayersOnPage.add(WhitelistedHashMap.get(Name));
        }

        UpdateSkins(PlayersOnPage, contents, p);

        //##################################################
        contents.fillRow(rows-1, ClickableItem.empty(Nix));
        contents.set(rows-1, 4, Back());
        contents.set(rows-1, 0, WhitelistStatus());
        contents.set(rows-1, 1, Reload());
        if (pagination.getPage() != 0) {
            contents.set(rows-1, 3, ClickableItem.of(Previous, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.previous().getPage());
                } else if (e.isRightClick()) {
                    open(p, pagination.first().getPage());
                }
            }));
        } else {
            contents.set(rows-1, 3, ClickableItem.of(Previous, e -> {
                if (e.isLeftClick()) {
                    open(p, (pagination.last().getPage()));
                } else if (e.isRightClick()) {
                    open(p, pagination.first().getPage());
                }
            }));
        }
        if (pagination.getPage() != pagination.last().getPage()) {
            contents.set(rows-1, 5, ClickableItem.of(Next, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.next().getPage());
                } else if (e.isRightClick()) {
                    open(p, (pagination.last().getPage()));
                }
            }));
        } else {
            contents.set(rows-1, 5, ClickableItem.of(Next, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.first().getPage());
                } else if (e.isRightClick()) {
                    open(p, pagination.last().getPage());
                }
            }));
        }
        pagination.first();
        if (currPage != 0) {
            for(int i = 0; i != currPage; i++) {
                pagination.next();
            }
        }
        //##################################################
        //TODO: Wenn ich auf enable gehe dann geht er immer auf seite 2 anstatt auf 1 zu bleiben
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

    private ClickableItem WhitelistStatus() {
        ItemStack item = new ItemStack(Material.GRAY_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.RED+"Whitelist");
        lore.add(ChatColor.GRAY+"Klicken um die Whitelist");
        lore.add(ChatColor.GRAY+"zu Aktivieren!");
        if (Bukkit.hasWhitelist()) {
            item = new ItemStack(Material.LIME_DYE);
            itemMeta.setDisplayName(ChatColor.GREEN+"Whitelist");
            lore.set(1, ChatColor.GRAY+"zu Deaktivieren!");
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (!Bukkit.hasWhitelist()) {
                Bukkit.setWhitelist(true);
            } else {
                Bukkit.setWhitelist(false);
            }
            Bukkit.reloadWhitelist();
            open(p, TempPage);
        });
        return ClickItem;
    }

    private ClickableItem Reload() {
        ItemStack item = new ItemStack(Material.PINK_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"Reload");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um die Whitelist");
        lore.add(ChatColor.GRAY+"zu Reloaden!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            Bukkit.reloadWhitelist();
            open(p, TempPage);
        });
        return ClickItem;
    }

    private ClickableItem SkullAdd(Player p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Hinzufügen");
        ClickableItem ClickItem = ClickableItem.of(getSkull(p, ChatColor.GREEN+p.getName(), lore), e -> {
            Add(p);
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
            Add(p);
        });
        return ClickItem;
    }

    private void Add(OfflinePlayer p) {
        //Add
    }

    private ClickableItem SkullRemove(OfflinePlayer p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Entfernen");
        ClickableItem ClickItem = ClickableItem.of(getSkullOffline(p, ChatColor.RED+p.getName(), lore), e -> {
            Remove(p);
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
            Remove(p);
        });
        return ClickItem;
    }

    private void Remove(OfflinePlayer p) {
        //Remove
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

    private void UpdateSkins(ArrayList<OfflinePlayer> Op, InventoryContents contents, Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(AdminPannel.plugin, new Runnable() {
            @Override
            public void run() {
                Integer row = 0;
                Integer column = 0;
                for (OfflinePlayer OpTemp : Op) {
                    contents.set(row, column, SkullRemove(OpTemp));
                    column++;
                    if (column == 9) {
                        column = 0;
                        row++;
                    }
                    p.updateInventory();
                }
            }
        });
    }
}
