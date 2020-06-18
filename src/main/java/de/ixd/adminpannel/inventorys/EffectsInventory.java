package de.ixd.adminpannel.inventorys;

import de.ixd.adminpannel.AdminPannel;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class EffectsInventory implements InventoryProvider {
    public void update(Player p, InventoryContents contents) {}


    private Integer duration = 1;
    private Integer amplifier = 1;

    private static SmartInventory EffectsInv = SmartInventory.builder().
            id("EffectsInv")
            .provider(new EffectsInventory())
            .title(ChatColor.GOLD+"Effects")
            .size(5,9)
            .build();

    public static void open(Player p) {
        EffectsInv.open(p);
    }

    public void init(Player p, InventoryContents contents) {
        ItemStack Nix = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta NixMeta = Nix.getItemMeta();
        NixMeta.setDisplayName(" ");
        Nix.setItemMeta(NixMeta);
        //##################################################
        addEffects(contents);
        contents.fillRow(EffectsInv.getRows()-1, ClickableItem.empty(Nix));
        contents.set(EffectsInv.getRows()-1, 2, Milk(p));
        contents.set(EffectsInv.getRows()-1, 4, Back());

    }

    private void addEffects(InventoryContents contents) {
        contents.add(Types("Absorption", "absorption", 9901901));
        contents.add(Types("Böses Omen", "bad_omen", 9901902));
        contents.add(Types("Blindheit", "blindness", 9901903));
        contents.add(Types("Meereskraft", "conduit_power", 9901904));
        contents.add(Types("Gunst des Delfins", "dolphins_grace", 9901905));
        contents.add(Types("Feuerschutz", "fire_resistance", 9901906));
        contents.add(Types("Leuchten", "glowing", 9901907));
        contents.add(Types("Eile", "FAST_DIGGING", 9901908));
        contents.add(Types("Extraenergie", "health_boost", 9901909));
        contents.add(Types("Held des Dorfes", "hero_of_the_village", 9901910));
        contents.add(Types("Hunger", "hunger", 9901911));
        contents.add(Types("Direktschaden", "HARM", 9901912));
        contents.add(Types("Direktheilung", "HEAL", 9901913));
        contents.add(Types("Unsichtbarkeit", "invisibility", 9901914));
        contents.add(Types("Sprungkraft", "JUMP", 9901915));
        contents.add(Types("Schwebekraft", "levitation", 9901916));
        contents.add(Types("Glück", "luck", 9901917));
        contents.add(Types("Abbaulähmung", "SLOW_DIGGING", 9901918));
        contents.add(Types("Übelkeit", "CONFUSION", 9901919));
        contents.add(Types("Nachtsicht", "night_vision", 9901920));
        contents.add(Types("Vergiftung", "poison", 9901921));
        contents.add(Types("Regeneration", "regeneration", 9901922));
        contents.add(Types("Resistenz", "DAMAGE_RESISTANCE", 9901923));
        contents.add(Types("Sättigung", "saturation", 9901924));
        contents.add(Types("Sanfter Fall", "slow_falling", 9901925));
        contents.add(Types("Langsamkeit", "SLOW", 9901926));
        contents.add(Types("Schnelligkeit", "speed", 9901927));
        contents.add(Types("Stärke", "INCREASE_DAMAGE", 9901928));
        contents.add(Types("Pech", "unluck", 9901929));
        contents.add(Types("Unterwasseratem", "water_breathing", 9901930));
        contents.add(Types("Schwäche", "weakness", 9901931));
        contents.add(Types("Ausdörrung", "wither", 9901932));

    }

    public ClickableItem Back() {
        ItemStack item = new ItemStack(Material.IRON_DOOR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE+"Zurück");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Zurück");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+":");
        lore.add(ChatColor.GRAY+"╰» "+ChatColor.BLUE+"Schließen");
        itemMeta.setLore(lore);
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

    public ClickableItem Types(String DisplayName, String Art, Integer ModelData) {

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+DisplayName);
        itemMeta.setCustomModelData(ModelData);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Klicken um den Effekt");
        lore.add(ChatColor.BLUE+DisplayName+ChatColor.GRAY+" zu bekommen!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            Player p = (Player) e.getWhoClicked();
            p.sendMessage("Effect: "+DisplayName+"  "+Art);
            p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(Art), 10 * 20, 0));
        });
        return ClickItem;
    }

    public ClickableItem Milk(Player p) {
        ItemStack item = new ItemStack(Material.MILK_BUCKET);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"Entferne alle Effecte!");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+p.getWorld().getName());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, new Consumer<InventoryClickEvent>() {
            @Override
            public void accept(InventoryClickEvent e) {
                Player p = (Player) e.getWhoClicked();
                for (PotionEffect effect : p.getActivePotionEffects()) {
                    p.sendMessage(effect.getType().getName());
                    p.removePotionEffect(effect.getType());
                }
            }
        });
        return ClickItem;
    }

    public ClickableItem duration(Player p) {
        ItemStack item = new ItemStack(Material.CHORUS_FRUIT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN+"Dauer");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW+"Current"+ChatColor.GRAY+": "+ChatColor.GOLD+duration);
        lore.add(ChatColor.GOLD+"Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 1");
        lore.add(ChatColor.GOLD+"Shift Links Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"+ 10");
        lore.add(ChatColor.GOLD+"Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 1");
        lore.add(ChatColor.GOLD+"Shift Rechts Klick"+ChatColor.GRAY+": "+ChatColor.BLUE+"- 10");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        ClickableItem ClickItem = ClickableItem.of(item, e -> {
            if (e.isShiftClick()) {
                if (e.isLeftClick()) {
                    if (duration < 244) {
                        duration = duration + 10;
                    }
                } else if (e.isRightClick()) {
                    if (duration > 10) {
                        duration = duration - 10;
                    }
                }
            } else {
                if (e.isLeftClick()) {
                    if (duration < 255) {
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
}
