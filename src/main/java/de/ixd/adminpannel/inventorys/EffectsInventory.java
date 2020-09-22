package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.function.Consumer;

public class EffectsInventory implements InventoryProvider {


    private Integer duration = 10;
    private Integer amplifier = 1;
    private Boolean Particles = true;
    private Player player = null;
    private int ticks = 0;

    private static SmartInventory EffectsInv = SmartInventory.builder().
            id("EffectsInv")
            .provider(new EffectsInventory())
            .title(ChatColor.GOLD+"Effects")
            .size(5,9)
            .build();

    public static void open(Player p) {
        EffectsInv.open(p);
    }

    public void update(Player p, InventoryContents contents) {
        if (ticks > 5) {
            addEffects(contents, p);
            ticks = 0;
        }
        ticks++;
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        if (player == null) {
            player = p;
        }
        //##################################################
        addEffects(contents, p);
        contents.fillRow(EffectsInv.getRows()-1, ClickableItem.empty(Nix));
        contents.set(EffectsInv.getRows()-1, 0, SelectedPlayerSkull(p));
        Bukkit.getScheduler().runTaskAsynchronously(AdminPannel.plugin, () -> {
            contents.set(EffectsInv.getRows()-1, 0, SelectedPlayer(p));
            p.updateInventory();
        });
        contents.set(EffectsInv.getRows()-1, 1, Milk(p));
        contents.set(EffectsInv.getRows()-1, 2, Reset(p));
        contents.set(EffectsInv.getRows()-1, 4, Back());
        contents.set(EffectsInv.getRows()-1, 6, Duration(p));
        contents.set(EffectsInv.getRows()-1, 7, Amplifier(p));
        contents.set(EffectsInv.getRows()-1, 8, hideParticles(p));

    }

    private void addEffects(InventoryContents contents, Player p) {
        //GOOD
        contents.set(0,0,Types("Absorption", "absorption", 9901901, p));
        contents.set(0,1,Types("Meereskraft", "conduit_power", 9901904, p));
        contents.set(0,2,Types("Gunst des Delfins", "dolphins_grace", 9901905, p));
        contents.set(0,3,Types("Feuerschutz", "fire_resistance", 9901906, p));
        contents.set(0,4,Types("Leuchten", "glowing", 9901907, p));
        contents.set(1,0,Types("Eile", "FAST_DIGGING", 9901908, p));
        contents.set(1,1,Types("Extraenergie", "health_boost", 9901909, p));
        contents.set(1,2,Types("Held des Dorfes", "hero_of_the_village", 9901910, p));
        contents.set(1,3,Types("Direktheilung", "HEAL", 9901913, p));
        contents.set(1,4,Types("Unsichtbarkeit", "invisibility", 9901914, p));
        contents.set(2,0,Types("Sprungkraft", "JUMP", 9901915, p));
        contents.set(2,1,Types("Glück", "luck", 9901917, p));
        contents.set(2,2,Types("Nachtsicht", "night_vision", 9901920, p));
        contents.set(2,3,Types("Regeneration", "regeneration", 9901922, p));
        contents.set(2,4,Types("Resistenz", "DAMAGE_RESISTANCE", 9901923, p));
        contents.set(3,0,Types("Sättigung", "saturation", 9901924, p));
        contents.set(3,1,Types("Sanfter Fall", "slow_falling", 9901925, p));
        contents.set(3,2,Types("Schnelligkeit", "speed", 9901927, p));
        contents.set(3,3,Types("Stärke", "INCREASE_DAMAGE", 9901928, p));
        contents.set(3,4,Types("Unterwasseratem", "water_breathing", 9901930, p));
        //####

        //#BAD
        contents.set(0,6,Types("Böses Omen", "bad_omen", 9901902, p));
        contents.set(0,7,Types("Blindheit", "blindness", 9901903, p));
        contents.set(0,8,Types("Hunger", "hunger", 9901911, p));
        contents.set(1,6,Types("Direktschaden", "HARM", 9901912, p));
        contents.set(1,7,Types("Schwebekraft", "levitation", 9901916, p));
        contents.set(1,8,Types("Abbaulähmung", "SLOW_DIGGING", 9901918, p));
        contents.set(2,6,Types("Übelkeit", "CONFUSION", 9901919, p));
        contents.set(2,7,Types("Vergiftung", "poison", 9901921, p));
        contents.set(2,8,Types("Langsamkeit", "SLOW", 9901926, p));
        contents.set(3,6,Types("Pech", "unluck", 9901929, p));
        contents.set(3,7,Types("Schwäche", "weakness", 9901931, p));
        contents.set(3,8,Types("Ausdörrung", "wither", 9901932, p));
        //####

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
                EffectsInv.close(p);
            }
        });
        return ClickItem;
    }

    public ClickableItem Types(String DisplayName, String Art, Integer ModelData, Player p) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+DisplayName);
        itemMeta.setCustomModelData(ModelData);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links"+ChatColor.GRAY+": "+ChatColor.DARK_PURPLE+"Selber");
        lore.add(ChatColor.GOLD+"Rechts"+ChatColor.GRAY+": "+ChatColor.DARK_PURPLE+player.getDisplayName());
        itemMeta.setLore(lore);

        if (p.getActivePotionEffects().size() != 0) {
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if( effect.getType().equals(PotionEffectType.getByName(Art)) ) {
                    itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
            }
        }

        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isLeftClick()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Art), duration * 20, amplifier-1, Particles));
            } else if (e.isRightClick()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Art), duration * 20, amplifier-1, Particles));
            }
        });
        return ClickItem;
    }

    public ClickableItem SelectedPlayerSkull(Player p) {
        ItemStack item = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Ausgewählter Spieler");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Ausgewählt"+ChatColor.GRAY+": "+ChatColor.AQUA+player.getDisplayName());
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.DARK_PURPLE+"Auswählen");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            select(p);
        });
        return ClickItem;
    }

    public ClickableItem SelectedPlayer(Player p) {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Ausgewählt"+ChatColor.GRAY+": "+ChatColor.AQUA+player.getDisplayName());
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.DARK_PURPLE+"Auswählen");
        ItemStack item = getSkull(player, ChatColor.BLUE+"Ausgewählter Spieler", lore);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            select(p);
        });
        return ClickItem;
    }

    private void select(Player p) {
        new AnvilGUI.Builder()
                .onClose(anvilp -> {
                    EffectsInv.close(anvilp);
                    Bukkit.getScheduler().runTask(AdminPannel.plugin, () -> {
                        EffectsInv.open(anvilp);
                    });
                })
                .onComplete((anvilp, text) -> {
                    for (Player bckp : Bukkit.getOnlinePlayers()) {
                        if (bckp.getDisplayName().equals(text)) {
                            player = bckp;
                            anvilp.sendMessage(AdminPannel.prefix+ChatColor.GREEN+"Spieler ausgewählt!");
                            return AnvilGUI.Response.close();
                        }
                    }
                    anvilp.sendMessage(AdminPannel.prefix+ChatColor.RED+"Spieler nicht Gefunden!");
                    return AnvilGUI.Response.text(text);
                })
                .text(player.getDisplayName())
                .item(getSkull(player, player.getDisplayName(), new ArrayList<String>()))
                .title(ChatColor.GOLD+"Spieler Auswählen")
                .plugin(AdminPannel.plugin)
                .open(p);
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

    public ClickableItem Milk(Player p) {
        ItemStack item = new ItemStack(Material.MILK_BUCKET);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Entferne alle Effekte!");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links"+ChatColor.GRAY+": "+ChatColor.DARK_PURPLE+"Selber");
        lore.add(ChatColor.GOLD+"Rechts"+ChatColor.GRAY+": "+ChatColor.DARK_PURPLE+player.getDisplayName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isLeftClick()) {
                Player p1 = (Player) e.getWhoClicked();
                for (PotionEffect effect : p1.getActivePotionEffects()) {
                    p1.removePotionEffect(effect.getType());
                }
            } else if (e.isRightClick()) {
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
            }
        });
        return ClickItem;
    }

    public ClickableItem Reset(Player p) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE+"Reset");
        itemMeta.setCustomModelData(9901809);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um alles auf");
        lore.add(ChatColor.GRAY+"die standard Werte");
        lore.add(ChatColor.GRAY+"zu stellen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            duration = 10;
            amplifier = 1;
            open(p);
        });
        return ClickItem;
    }

    public ClickableItem Duration(Player p) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Dauer");
        itemMeta.setCustomModelData(9901935);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+duration+ChatColor.GRAY+" Sekunden");
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 1");
        lore.add(ChatColor.GOLD+"Shift Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 10");
        lore.add(ChatColor.GOLD+"Middle Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"= 1000000");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 1");
        lore.add(ChatColor.GOLD+"Shift Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 10");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.getClick().equals(ClickType.MIDDLE)) {
                duration = 1000000;
            }
            if (e.isShiftClick()) {
                if (e.isLeftClick()) {
                    if (duration < 3591) {
                        duration = duration + 10;
                    }
                } else if (e.isRightClick()) {
                    if (duration > 10) {
                        duration = duration - 10;
                    }
                }
            } else {
                if (e.isLeftClick()) {
                    if (duration < 3600) {
                        duration = duration + 1;
                    }

                } else if (e.isRightClick()) {
                    if (duration > 1) {
                        duration = duration - 1;
                    }
                }
            }
            EffectsInv.open(p);
        });
        return ClickItem;
    }

    public ClickableItem Amplifier(Player p) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Stärke");
        itemMeta.setCustomModelData(9901936);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+amplifier);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 1");
        lore.add(ChatColor.GOLD+"Shift Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 10");
        lore.add(ChatColor.GOLD+"Middle Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"= 256");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 1");
        lore.add(ChatColor.GOLD+"Shift Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 10");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.getClick().equals(ClickType.MIDDLE)) {
                amplifier = 256;
            }
            if (e.isShiftClick()) {
                if (e.isLeftClick()) {
                    if (amplifier < 246) {
                        amplifier = amplifier + 10;
                    }
                } else if (e.isRightClick()) {
                    if (amplifier > 10) {
                        amplifier = amplifier - 10;
                    }
                }
            } else {
                if (e.isLeftClick()) {
                    if (amplifier < 256) {
                        amplifier = amplifier + 1;
                    }

                } else if (e.isRightClick()) {
                    if (amplifier > 1) {
                        amplifier = amplifier - 1;
                    }
                }
            }
            EffectsInv.open(p);
        });
        return ClickItem;
    }

    private ClickableItem hideParticles(Player p) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        itemMeta.setDisplayName(ChatColor.RED+"Partikel");
        itemMeta.setCustomModelData(9901933);
        lore.add(ChatColor.GRAY+"Klicken um die Partikel");
        lore.add(ChatColor.GRAY+"zu Aktivieren!");
        if (!Particles) {
            itemMeta.setCustomModelData(9901934);
            itemMeta.setDisplayName(ChatColor.GREEN+"Partikel");
            lore.set(1, ChatColor.GRAY+"zu Deaktivieren!");
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (Particles) {
                Particles = false;
            } else {
                Particles = true;
            }
            open(p);
        });
        return ClickItem;
    }
}
