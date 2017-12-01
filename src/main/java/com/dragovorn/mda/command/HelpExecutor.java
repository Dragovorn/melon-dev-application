package com.dragovorn.mda.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static com.dragovorn.mda.util.ChatHelper.colourize;

public class HelpExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage(colourize("&6&m------------------------&r &aHelp &6&m------------------------"));
        sender.sendMessage(colourize("&e/help    &6- &aDisplay this message"));
        sender.sendMessage(colourize("&e/lock    &6- &aLocks the chest/furnace/door you are looking at"));
        sender.sendMessage(colourize("&e/unlock &6- &aUnlocks the chest/furnace/door you are looking at"));
        sender.sendMessage(colourize("&6&m------------------------&r &aHelp &6&m------------------------"));

        return true;
    }
}
