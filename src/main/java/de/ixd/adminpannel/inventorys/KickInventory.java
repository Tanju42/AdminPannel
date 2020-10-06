package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class KickInventory implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}


    private static Pagination pagination = null;
    private static Integer rows = 2;
    private static Integer TempPage = 0;

    private static SmartInventory KickInv = SmartInventory.builder().
            id("KickInv")
            .provider(new KickInventory())
            .title(ChatColor.GOLD+"Kick "+ChatColor.GRAY+"|"+ChatColor.AQUA+" 0")
            .size(rows,9)
            .build();

    public static void openHead(Player p, Integer Page) {
        rows = 2;
        System.out.println("HIER  asd");
        for (int tempRows = 9; tempRows < Bukkit.getOnlinePlayers().size(); tempRows = tempRows + 9) {
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
        KickInv = SmartInventory.builder().
                id("KickInv")
                .provider(new KickInventory())
                .title(ChatColor.GOLD+"Kick "+ChatColor.GRAY+"| "+ChatColor.AQUA+TempPage)
                .size(rows,9)
                .build();
        KickInv.open(p, TempPage);
    }

    static boolean click = false;

    public static void openName(Player p) {
        new AnvilGUI.Builder()
                .onClose(anvilp -> {
                    if (!click) {
                        anvilp.closeInventory();
                        Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                            MainInventory.open(anvilp);
                        });
                    }
                    click = false;
                })
                .onComplete((anvilp, text) -> {
                    click = true;
                    Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                        SmartInventory addConfirm = SmartInventory.builder()
                                .manager(AdminPannel.SmartInvsMan)
                                .size(1, 9)
                                .title(ChatColor.RED+"Kicken "+ChatColor.GRAY+"| "+ChatColor.AQUA+text)
                                .provider((p1, inventoryContents) -> {
                                    ItemStack Nix = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                                    ItemMeta NixMeta = Nix.getItemMeta();
                                    NixMeta.setDisplayName(" ");
                                    Nix.setItemMeta(NixMeta);
                                    inventoryContents.fill(ClickableItem.empty(Nix));
                                    //####
                                    ItemStack Confirm = new ItemStack(Material.PAPER);
                                    ItemMeta ConfirmMeta = Nix.getItemMeta();
                                    ConfirmMeta.setDisplayName(ChatColor.GREEN+"Bestätigen");
                                    ConfirmMeta.setCustomModelData(9901804);
                                    Confirm.setItemMeta(ConfirmMeta);
                                    inventoryContents.set(0, 2, ClickableItem.of(Confirm, e2 -> {
                                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kick "+text);
                                        Bukkit.reloadWhitelist();
                                        e2.getWhoClicked().sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Spieler "+ChatColor.AQUA+text+ChatColor.YELLOW+" wurde gekickt!");
                                        MainInventory.open((Player) e2.getWhoClicked());
                                    }));
                                    //####
                                    ArrayList<String> lore2 = new ArrayList<>();
                                    lore2.add(ChatColor.GOLD+"Wirklich den Spieler");
                                    lore2.add(ChatColor.AQUA+text+ChatColor.GREEN+" Kicken"+ChatColor.GOLD+"?");
                                    inventoryContents.set(0, 4, ClickableItem.empty(getSkull(Bukkit.getPlayer(text), ChatColor.AQUA+text, lore2)));
                                    //####
                                    ItemStack Cancel = new ItemStack(Material.PAPER);
                                    ItemMeta CancelMeta = Nix.getItemMeta();
                                    CancelMeta.setDisplayName(ChatColor.RED+"Abbrechen");
                                    CancelMeta.setCustomModelData(9901805);
                                    Cancel.setItemMeta(CancelMeta);
                                    inventoryContents.set(0, 6, ClickableItem.of(Cancel, e2 -> {
                                        MainInventory.open(anvilp);
                                    }));
                                    //####

                                })
                                .build();
                        addConfirm.open(anvilp);
                    });
                    return AnvilGUI.Response.text("");
                })
                .text(p.getDisplayName())
                .item(new ItemStack(Material.PAPER))
                .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"|"+ChatColor.GREEN+" Hinzufügen")
                .plugin(AdminPannel.plugin)
                .open(p);
    }

    public void init(Player p, InventoryContents contents) {
        if (pagination == null) {
            pagination = contents.pagination();
        }
        Integer currPage = pagination.getPage();
        //##################################################
        ItemStack Nix = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        ItemStack Previous = new ItemStack(Material.PAPER);
        ItemMeta PreviousMeta = Nix.getItemMeta();
        PreviousMeta.setDisplayName(ChatColor.DARK_AQUA+"Previous");
        PreviousMeta.setCustomModelData(9901807);
        ArrayList<String> Backlore = new ArrayList<>();
        Backlore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.DARK_AQUA+"First");
        PreviousMeta.setLore(Backlore);
        Previous.setItemMeta(PreviousMeta);
        //##################################################
        ItemStack Next = new ItemStack(Material.PAPER);
        ItemMeta NextMeta = Nix.getItemMeta();
        NextMeta.setDisplayName(ChatColor.DARK_AQUA+"Next");
        NextMeta.setCustomModelData(9901806);
        ArrayList<String> Nextlore = new ArrayList<>();
        Nextlore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.DARK_AQUA+"Last");
        NextMeta.setLore(Nextlore);
        Next.setItemMeta(NextMeta);
        //##################################################
        HashMap<String, Player> KickHashMap = new HashMap<>();
        // <Name, OfflinePlayer>
        for (Player Op : Bukkit.getOnlinePlayers()) {
            KickHashMap.put(Op.getName(), Op);
        }
        ArrayList<String> KickPlayers = new ArrayList<>(KickHashMap.keySet());
        Collections.sort(KickPlayers);


        ClickableItem[] items = new ClickableItem[KickPlayers.size()];

        for(int i = 0; i < items.length; i++) {
            items[i] = TempSkullRemove(KickHashMap.get(KickPlayers.get(i)));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(45);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        ArrayList<Player> PlayersOnPage = new ArrayList<>();
        Integer from = (pagination.getPage() * 45);
        Integer to = ((pagination.getPage()+1) * 45);
        if (KickPlayers.size() < to) {
            to = KickPlayers.size();
        }
        List<String> PlayerNamesOnPage = KickPlayers.subList(from, to);
        for (String Name : PlayerNamesOnPage) {
            PlayersOnPage.add(KickHashMap.get(Name));
        }

        UpdateSkins(PlayersOnPage, contents, p);

        //##################################################
        contents.fillRow(rows-1, ClickableItem.empty(Nix));
        contents.set(rows-1, 4, Back());
        //##################################################
        page(contents, Previous, Next, p, currPage);
    }

    private void page(InventoryContents contents, ItemStack Previous, ItemStack Next, Player p, Integer currPage) {
        if (pagination.getPage() != 0) {
            contents.set(rows-1, 3, ClickableItem.of(Previous, e -> {
                if (e.isLeftClick()) {
                    openHead(p, pagination.previous().getPage());
                } else if (e.isRightClick()) {
                    openHead(p, pagination.first().getPage());
                }
            }));
        } else {
            contents.set(rows-1, 3, ClickableItem.of(Previous, e -> {
                if (e.isLeftClick()) {
                    openHead(p, (pagination.last().getPage()));
                } else if (e.isRightClick()) {
                    openHead(p, pagination.first().getPage());
                }
            }));
        }
        if (pagination.getPage() != pagination.last().getPage()) {
            contents.set(rows-1, 5, ClickableItem.of(Next, e -> {
                if (e.isLeftClick()) {
                    openHead(p, pagination.next().getPage());
                } else if (e.isRightClick()) {
                    openHead(p, (pagination.last().getPage()));
                }
            }));
        } else {
            contents.set(rows-1, 5, ClickableItem.of(Next, e -> {
                if (e.isLeftClick()) {
                    openHead(p, pagination.first().getPage());
                } else if (e.isRightClick()) {
                    openHead(p, pagination.last().getPage());
                }
            }));
        }
        pagination.first();
        if (currPage != 0) {
            for(int i = 0; i != currPage; i++) {
                pagination.next();
            }
        }
    }

    private ClickableItem Back() {
        ItemStack item = new ItemStack(Material.IRON_DOOR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE+"Zurück");
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            MainInventory.open(p);
        });
        return ClickItem;
    }

    private ClickableItem SkullRemove(Player p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Kick");
        ClickableItem ClickItem = ClickableItem.of(getSkull(p, ChatColor.AQUA+p.getName(), lore), e -> {
            Remove(p);
        });
        return ClickItem;
    }

    private ClickableItem TempSkullRemove(Player p) {
        ItemStack item = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA+p.getName());
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Kick");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Remove(p);
        });
        return ClickItem;
    }

    private void Remove(Player p) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kick "+p.getName());
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

    private void UpdateSkins(ArrayList<Player> Op, InventoryContents contents, Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(AdminPannel.plugin, new Runnable() {
            @Override
            public void run() {
                Integer row = 0;
                Integer column = 0;
                for (Player OpTemp : Op) {
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
