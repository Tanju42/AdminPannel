package de.ixd.adminpannel;

import de.ixd.adminpannel.inventorys.MainInventory;
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
            } else {
                p.sendMessage(ChatColor.RED+"too many arguments!");
            }
            return true;
        } else {
            return false;
        }
    }
}
