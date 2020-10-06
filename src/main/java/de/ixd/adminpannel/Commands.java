package de.ixd.adminpannel;

import de.ixd.adminpannel.inventorys.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command command, String cmd, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (args.length == 0) {
                MainInventory.open(p);
            } else if (args.length == 1) {
                if (args[0].equals("weather")) {
                    WeatherInventory.open(p);
                } else if (args[0].equals("gamerules")) {
                    GameRulesInventory.open(p);
                } else if (args[0].equals("sounds")) {
                    SoundsInventory.open(p, 0, 1, 1);
                } else if (args[0].equals("sup")) {
                    SupporterInventory.open(p);
                }
            } else if (args.length == 2) {

            } else {
                p.sendMessage(ChatColor.RED+"too many arguments!");
            }
            return true;
        } else {
            return false;
        }
    }
}
