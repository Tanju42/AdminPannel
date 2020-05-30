package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SoundsInventory implements InventoryProvider {

    private static ArrayList<Sound> PlayingSounds = new ArrayList<Sound>();
    private static Pagination pagination = null;

    private double volume = 1.0;
    private double pitch = 1.0;

    private static SmartInventory SoundsInv = SmartInventory.builder().
            id("SoundsInv")
            .provider(new SoundsInventory())
            .title(ChatColor.GOLD+"Sounds "+ChatColor.GRAY+"| "+ChatColor.AQUA+"0")
            .size(6,9)
            .build();

    public static void open(Player p, Integer Page) {
        if (pagination != null) {
            SoundsInv = SmartInventory.builder().
                    id("SoundsInv")
                    .provider(new SoundsInventory())
                    .title(ChatColor.GOLD+"Sounds "+ChatColor.GRAY+"| "+ChatColor.AQUA+pagination.getPage())
                    .size(6,9)
                    .build();
        }
        SoundsInv.open(p, Page);
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        ItemStack Back = new ItemStack(Material.ARROW);
        ItemMeta BackMeta = Nix.getItemMeta();
        BackMeta.setDisplayName(ChatColor.DARK_AQUA+"Back");
        ArrayList<String> Backlore = new ArrayList<>();
        Backlore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.DARK_AQUA+"First");
        BackMeta.setLore(Backlore);
        Back.setItemMeta(BackMeta);
        //##################################################
        ItemStack Next = new ItemStack(Material.ARROW);
        ItemMeta NextMeta = Nix.getItemMeta();
        NextMeta.setDisplayName(ChatColor.DARK_AQUA+"Next");
        ArrayList<String> Nextlore = new ArrayList<>();
        Nextlore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.DARK_AQUA+"Last");
        NextMeta.setLore(Nextlore);
        Next.setItemMeta(NextMeta);
        //##################################################
        pagination = contents.pagination();

        contents.fillRow(5, ClickableItem.empty(Nix));
        contents.set(5, 0, Volume2(p, pagination.getPage()));
        contents.set(5, 1, Volume(p, pagination.getPage()));
        contents.set(5, 2, Stop(p, pagination.getPage()));

        if (pagination.getPage() != 0) {
            contents.set(5, 3, ClickableItem.of(Back, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.previous().getPage());
                } else if (e.isRightClick()) {
                    open(p, pagination.first().getPage());
                }
            }));
        }
        contents.set(5, 4, Back());
        if (pagination.getPage() != 17) {
            contents.set(5, 5, ClickableItem.of(Next, e -> {
                if (e.isLeftClick()) {
                    open(p, pagination.next().getPage());
                } else if (e.isRightClick()) {
                    open(p, (pagination.last().getPage()-1));
                }
            }));
        }
        contents.set(5, 6, Reset(p, pagination.getPage()));
        contents.set(5, 7, Pitch(p, pagination.getPage()));
        contents.set(5, 8, Pitch2(p, pagination.getPage()));

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

    public ClickableItem Stop(Player p, Integer Page) {
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
        ItemStack item = new ItemStack(Material.PINK_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"Reset");
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
        ItemStack item = new ItemStack(Material.CHORUS_FRUIT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Lautstärke");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+volume);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 1");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 1");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isLeftClick()) {
                if (volume < 99) {
                    volume = volume + 1;
                }

            } else if (e.isRightClick()) {
                if (volume > 1) {
                    volume = volume - 1;
                }
            }
            SoundsInv.open(p, Page);
        });
        return ClickItem;
    }

    public ClickableItem Volume2(Player p, Integer Page) {
        ItemStack item = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Lautstärke");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+volume);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 0.1");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 0.1");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isLeftClick()) {
                if (volume < 99.9) {
                    volume = volume + 0.1;
                }

            } else if (e.isRightClick()) {
                if (volume > 0.1) {
                    volume = volume - 0.1;
                }
            }
            SoundsInv.open(p, Page);
        });
        return ClickItem;
    }

    public ClickableItem Pitch(Player p, Integer Page) {
        ItemStack item = new ItemStack(Material.CHORUS_FRUIT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Pitch");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+pitch);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 1");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 1");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isLeftClick()) {
                if (pitch < 99) {
                    pitch = pitch + 1;
                }
            } else if (e.isRightClick()) {
                if (pitch > 1) {
                    pitch = pitch - 1;
                }
            }
            SoundsInv.open(p, Page);
        });
        return ClickItem;
    }

    public ClickableItem Pitch2(Player p, Integer Page) {
        ItemStack item = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Pitch");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+pitch);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 0.1");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 0.1");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isLeftClick()) {
                if (pitch < 99.9) {
                    pitch = Math.round((pitch + 0.1)*100)/100;
                }
            } else if (e.isRightClick()) {
                if (pitch > 0.1) {
                    pitch = Math.round((pitch - 0.1)*100)/100;
                }
            }
            SoundsInv.open(p, Page);
        });
        return ClickItem;
    }
}
