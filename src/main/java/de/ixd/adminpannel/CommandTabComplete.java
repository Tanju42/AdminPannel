package de.ixd.adminpannel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender cs, Command command, String s, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            final String[] cmds = { "weather", "gamerules", "sounds" };
            StringUtil.copyPartialMatches(args[0], Arrays.asList(cmds), completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
