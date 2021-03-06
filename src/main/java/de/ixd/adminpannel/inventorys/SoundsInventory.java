package de.ixd.adminpannel.inventorys;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SoundsInventory implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}

    private static ArrayList<Sound> PlayingSounds = new ArrayList<Sound>();
    private static Pagination pagination = null;

    private static double volume = 1.0;
    private static double pitch = 1.0;

    public static SmartInventory SoundsInv = SmartInventory.builder().
            id("SoundsInv")
            .provider(new SoundsInventory())
            .title(ChatColor.GOLD+"Sounds "+ChatColor.GRAY+"| "+ChatColor.AQUA+"0")
            .size(6,9)
            .build();

    public static void open(Player p, Integer Page, double vol, double pit) {
        if (pagination != null) {
            SoundsInv = SmartInventory.builder().
                    id("SoundsInv")
                    .provider(new SoundsInventory())
                    .title(ChatColor.GOLD+"Sounds "+ChatColor.GRAY+"| "+ChatColor.AQUA+pagination.getPage())
                    .size(6,9)
                    .build();
        }
        volume = vol;
        pitch = pit;
        SoundsInv.open(p, Page);
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
        contents.fillRow(5, ClickableItem.empty(Nix));
        contents.set(5, 1, Volume(p, pagination.getPage()));
        contents.set(5, 2, Stop(p));

        if (pagination.getPage() != 0) {
            contents.set(5, 3, ClickableItem.of(Previous, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.previous().getPage(), volume, pitch);
                } else if (e.isRightClick()) {
                    open(p, pagination.first().getPage(), volume, pitch);
                }
            }));
        } else {
            contents.set(5, 3, ClickableItem.of(Previous, e -> {
                if (e.isLeftClick()) {
                    pagination.last();
                    pagination.previous();
                    open(p, (pagination.getPage()), volume, pitch);
                } else if (e.isRightClick()) {
                    open(p, pagination.first().getPage(), volume, pitch);
                }
            }));
        }
        contents.set(5, 4, Back());
        if (pagination.getPage() != 17) {
            contents.set(5, 5, ClickableItem.of(Next, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.next().getPage(), volume, pitch);
                } else if (e.isRightClick()) {
                    pagination.last();
                    pagination.previous();
                    open(p, (pagination.getPage()), volume, pitch);
                }
            }));
        } else {
            contents.set(5, 5, ClickableItem.of(Next, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.first().getPage(), volume, pitch);
                } else if (e.isRightClick()) {
                    pagination.last();
                    pagination.previous();
                    open(p, (pagination.getPage()), volume, pitch);
                }
            }));
        }
        //##################################################
        contents.set(5, 6, Reset(p, pagination.getPage()));
        contents.set(5, 7, Pitch(p, pagination.getPage()));

        ClickableItem[] items = new ClickableItem[Sound.values().length];

        Material itemMaterial = Material.PAPER;
        for(int i = 0; i < items.length; i++) {
            if (itemMaterial.equals(Material.PAPER)) {
                itemMaterial = Material.MAP;
            } else {
                itemMaterial = Material.PAPER;
            }
            ItemStack item = new ItemStack(itemMaterial);
            ItemMeta itemMeta = Nix.getItemMeta();
            Sound[] Sounds = Sound.values();
            itemMeta.setDisplayName(ChatColor.BLUE+String.valueOf(Sounds[i]));
            item.setItemMeta(itemMeta);
            final Integer number = i;
            items[i] = ClickableItem.of(item, e -> {
                Sound currSound = Sound.valueOf(String.valueOf(Sounds[number]));
                p.playSound(p.getLocation(), currSound, (float) volume, (float) pitch);
                PlayingSounds.add(currSound);
            });
        }
        pagination.setItems(items);
        pagination.setItemsPerPage(45);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));


        pagination.first();
        if (currPage != 0) {
            for(int i = 0; i != currPage; i++) {
                pagination.next();
            }
        }
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
                SoundsInv.close(p);
            }
        });
        return ClickItem;
    }

    public ClickableItem Stop(Player p) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED+"Stop");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um die ");
        lore.add(ChatColor.GRAY+"Sounds zu Stoppen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            for (Sound sound : PlayingSounds) {
                p.stopSound(sound);
            }
            PlayingSounds.clear();
        });
        return ClickItem;
    }

    public ClickableItem Reset(Player p, Integer Page) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"Reset");
        itemMeta.setCustomModelData(9901809);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um Lautstärke");
        lore.add(ChatColor.GRAY+"und Pitch zu Reseten!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            volume = 1.0;
            pitch = 1.0;
            SoundsInv.open(p, Page);
        });
        return ClickItem;
    }

    public ClickableItem Volume(Player p, Integer Page) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Lautstärke");
        itemMeta.setCustomModelData(9903901);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+volume);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 1");
        lore.add(ChatColor.GOLD+"Shift Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 0.1");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 1");
        lore.add(ChatColor.GOLD+"Shift Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 0.1");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isShiftClick()) {
                if (e.isLeftClick()) {
                    if (volume < 99.9) {
                        volume = volume * 100;
                        volume = volume + 10;
                        volume = volume / 100;
                    }
                } else if (e.isRightClick()) {
                    if (volume > 0.1) {
                        volume = volume * 100;
                        volume = volume - 10;
                        volume = volume / 100;
                    }
                }
            } else {
                if (e.isLeftClick()) {
                    if (volume < 99) {
                        volume = volume + 1;
                    }

                } else if (e.isRightClick()) {
                    if (volume > 1) {
                        volume = volume - 1;
                    }
                }
            }
            volume = Double.parseDouble(String.valueOf(volume).substring(0, 3));
            SoundsInv.open(p, Page);
        });
        return ClickItem;
    }

    public ClickableItem Pitch(Player p, Integer Page) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Pitch");
        itemMeta.setCustomModelData(9903902);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+pitch);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 1");
        lore.add(ChatColor.GOLD+"Shift Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 0.1");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 1");
        lore.add(ChatColor.GOLD+"Shift Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 0.1");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isShiftClick()) {
                if (e.isLeftClick()) {
                    if (pitch < 99.9) {
                        pitch = pitch + 0.1;
                    }
                } else if (e.isRightClick()) {
                    if (pitch > 0.1) {
                        pitch = pitch - 0.1;
                    }
                }
            } else {
                if (e.isLeftClick()) {
                    if (pitch < 99) {
                        pitch = pitch + 1;
                    }

                } else if (e.isRightClick()) {
                    if (pitch > 1) {
                        pitch = pitch - 1;
                    }
                }
            }
            pitch = Double.parseDouble(String.valueOf(pitch).substring(0, 3));
            SoundsInv.open(p, Page);
        });
        return ClickItem;
    }
}
