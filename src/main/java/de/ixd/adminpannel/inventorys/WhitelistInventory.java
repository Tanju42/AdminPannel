package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
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
        HashMap<String, OfflinePlayer> WhitelistedHashMap = new HashMap<>();
        // <Name, OfflinePlayer>
        for (OfflinePlayer Op : Bukkit.getWhitelistedPlayers()) {
            WhitelistedHashMap.put(Op.getName(), Op);
        }
        ArrayList<String> WhitelistedPlayers = new ArrayList<>(WhitelistedHashMap.keySet());
        Collections.sort(WhitelistedPlayers);


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
        contents.set(rows-1, 0, WhitelistStatus());
        contents.set(rows-1, 1, Reload());
        contents.set(rows-1, 4, Back());
        contents.set(rows-1, 7, AddItem());
        contents.set(rows-1, 8, RemoveItem());
        //##################################################
        page(contents, Previous, Next, p, currPage);
    }

    private ClickableItem AddItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Hinzufügen");
        itemMeta.setCustomModelData(9901802);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Mit Kopf");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Mit Name");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isLeftClick()) {
                SmartInventory addHeadInv = SmartInventory.builder()
                        .manager(AdminPannel.SmartInvsMan)
                        .size(2, 9)
                        .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"|"+ChatColor.GREEN+" Hinzufügen")
                        .provider(new InventoryProvider() {
                                      @Override
                                      public void init(Player player, InventoryContents contents) {
                                          //########################
                                          ArrayList < String > lore1 = new ArrayList<>();
                                          lore1.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
                                          lore1.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Hinzufügen");
                                          contents.add(ClickableItem.of(getSkull(p, ChatColor.GREEN+p.getName(), lore1), e1 -> {
                                              SmartInventory addConfirm = SmartInventory.builder()
                                                      .manager(AdminPannel.SmartInvsMan)
                                                      .size(1, 9)
                                                      .title(ChatColor.GREEN+"Hinzufügen "+ChatColor.GRAY+"| "+ChatColor.AQUA+p.getName())
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
                                                              Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist add "+e2.getWhoClicked().getName());
                                                              Bukkit.reloadWhitelist();
                                                              p.sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Spieler "+ChatColor.AQUA+e2.getWhoClicked().getName()+ChatColor.YELLOW+" wurde zur Whitelist hinzugefügt!");
                                                              WhitelistInv.open((Player) e2.getWhoClicked());
                                                          }));
                                                          //####
                                                          ArrayList<String> lore2 = new ArrayList<>();
                                                          lore2.add(ChatColor.GOLD+"Wirklich den Spieler");
                                                          lore2.add(ChatColor.AQUA+p.getName()+ChatColor.GREEN+" hinzufügen"+ChatColor.GOLD+"?");
                                                          inventoryContents.set(0, 4, ClickableItem.empty(getSkull(p, ChatColor.AQUA+p.getName(), lore2)));
                                                          //####
                                                          ItemStack Cancel = new ItemStack(Material.PAPER);
                                                          ItemMeta CancelMeta = Nix.getItemMeta();
                                                          CancelMeta.setDisplayName(ChatColor.RED+"Abbrechen");
                                                          CancelMeta.setCustomModelData(9901805);
                                                          Cancel.setItemMeta(CancelMeta);
                                                          inventoryContents.set(0, 6, ClickableItem.of(Cancel, e2 -> {
                                                              WhitelistInv.open((Player) e2.getWhoClicked());
                                                          }));
                                                          //####

                                                      })
                                                      .build();
                                              addConfirm.open(p);
                                          }));
                                          //########################
                                          ItemStack Nix = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                                          ItemMeta NixMeta = Nix.getItemMeta();
                                          NixMeta.setDisplayName(" ");
                                          Nix.setItemMeta(NixMeta);
                                          //##################################################
                                          contents.fillRow(1, ClickableItem.empty(Nix));
                                          //####
                                          ItemStack item1 = new ItemStack(Material.IRON_DOOR);
                                          ItemMeta itemMeta1 = item1.getItemMeta();
                                          itemMeta1.setDisplayName(ChatColor.DARK_PURPLE+"Zurück");
                                          item1.setItemMeta(itemMeta1);
                                          //####
                                          contents.set(1, 4, ClickableItem.of(item1, e1 -> {
                                              Player p1 = (Player) e1.getWhoClicked();
                                              WhitelistInv.open(p1);
                                          }));
                                      }
                        })
                        .build();
                addHeadInv.open(p);
            } else if (e.isRightClick()) {
                new AnvilGUI.Builder()
                        .onClose(anvilp -> {
                            if (!click) {
                                anvilp.closeInventory();
                                Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                                    WhitelistInv.open(anvilp);
                                });
                            }
                            click = false;
                        })
                        .onComplete((anvilp, text) -> {
                            click = true;
                            Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                                Add(anvilp, Bukkit.getOfflinePlayer(text));
                            });
                            return AnvilGUI.Response.text("");
                        })
                        .text(p.getDisplayName())
                        .item(new ItemStack(Material.PAPER))
                        .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"|"+ChatColor.GREEN+" Hinzufügen")
                        .plugin(AdminPannel.plugin)
                        .open(p);
            }
        });
        return ClickItem;
    }

    static boolean click = false;

    private ClickableItem RemoveItem() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED+"Entfernen");
        itemMeta.setCustomModelData(9901803);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Mit Name");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            if (e.isRightClick()) {
                new AnvilGUI.Builder()
                        .onClose(anvilp -> {
                            if (!click) {
                                anvilp.closeInventory();
                                Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                                    WhitelistInv.open(anvilp);
                                });
                            }
                            click = false;
                        })
                        .onComplete((anvilp, text) -> {
                            click = true;
                            Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                                Remove(anvilp, Bukkit.getOfflinePlayer(text));
                            });
                            return AnvilGUI.Response.text("");
                        })
                        .text(p.getDisplayName())
                        .item(new ItemStack(Material.PAPER))
                        .title(ChatColor.GOLD+"Whitelist "+ChatColor.GRAY+"|"+ChatColor.RED+" Entfernen")
                        .plugin(AdminPannel.plugin)
                        .open(p);
            }
        });
        return ClickItem;
    }

    private void page(InventoryContents contents, ItemStack Previous, ItemStack Next, Player p, Integer currPage) {
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
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"Reload");
        itemMeta.setCustomModelData(9901808);
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

    private ClickableItem SkullRemove(OfflinePlayer p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Entfernen");
        ClickableItem ClickItem = ClickableItem.of(getSkullOffline(p, ChatColor.RED+p.getName(), lore), e -> {
            Remove((Player) e.getWhoClicked(), p);
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
            Remove((Player) e.getWhoClicked(), p);
        });
        return ClickItem;
    }

    private void Remove(Player clicker, OfflinePlayer p) {
        SmartInventory removeConfirm = SmartInventory.builder()
                .manager(AdminPannel.SmartInvsMan)
                .size(1, 9)
                .title(ChatColor.RED+"Entfernen "+ChatColor.GRAY+"| "+ChatColor.AQUA+p.getName())
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
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist remove "+p.getName());
                        Bukkit.reloadWhitelist();
                        e2.getWhoClicked().sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Spieler "+ChatColor.AQUA+p.getName()+ChatColor.YELLOW+" wurde von der Whitelist entfernt!");
                        WhitelistInv.open((Player) e2.getWhoClicked());
                    }));
                    //####
                    ArrayList<String> lore2 = new ArrayList<>();
                    lore2.add(ChatColor.GOLD+"Wirklich den Spieler");
                    lore2.add(ChatColor.AQUA+p.getName()+ChatColor.RED+" Entfernen"+ChatColor.GOLD+"?");
                    inventoryContents.set(0, 4, ClickableItem.empty(getSkullOffline(p, ChatColor.AQUA+p.getName(), lore2)));
                    //####
                    ItemStack Cancel = new ItemStack(Material.PAPER);
                    ItemMeta CancelMeta = Nix.getItemMeta();
                    CancelMeta.setDisplayName(ChatColor.RED+"Abbrechen");
                    CancelMeta.setCustomModelData(9901805);
                    Cancel.setItemMeta(CancelMeta);
                    inventoryContents.set(0, 6, ClickableItem.of(Cancel, e2 -> {
                        WhitelistInv.open((Player) e2.getWhoClicked());
                    }));
                    //####

                })
                .build();
        removeConfirm.open(clicker);
    }

    private void Add(Player clicker, OfflinePlayer p) {
        SmartInventory addConfirm = SmartInventory.builder()
                .manager(AdminPannel.SmartInvsMan)
                .size(1, 9)
                .title(ChatColor.GREEN+"Hinzufügen "+ChatColor.GRAY+"| "+ChatColor.AQUA+p.getName())
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
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist add "+p.getName());
                        Bukkit.reloadWhitelist();
                        e2.getWhoClicked().sendMessage(AdminPannel.prefix+ChatColor.YELLOW+"Spieler "+ChatColor.AQUA+p.getName()+ChatColor.YELLOW+" wurde der Whitelist hinzugefügt!");
                        WhitelistInv.open((Player) e2.getWhoClicked());
                    }));
                    //####
                    ArrayList<String> lore2 = new ArrayList<>();
                    lore2.add(ChatColor.GOLD+"Wirklich den Spieler");
                    lore2.add(ChatColor.AQUA+p.getName()+ChatColor.GREEN+" Hinzufügen"+ChatColor.GOLD+"?");
                    inventoryContents.set(0, 4, ClickableItem.empty(getSkullOffline(p, ChatColor.AQUA+p.getName(), lore2)));
                    //####
                    ItemStack Cancel = new ItemStack(Material.PAPER);
                    ItemMeta CancelMeta = Nix.getItemMeta();
                    CancelMeta.setDisplayName(ChatColor.RED+"Abbrechen");
                    CancelMeta.setCustomModelData(9901805);
                    Cancel.setItemMeta(CancelMeta);
                    inventoryContents.set(0, 6, ClickableItem.of(Cancel, e2 -> {
                        WhitelistInv.open((Player) e2.getWhoClicked());
                    }));
                    //####

                })
                .build();
        addConfirm.open(clicker);
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
