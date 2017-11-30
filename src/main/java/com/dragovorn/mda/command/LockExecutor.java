package com.dragovorn.mda.command;

import com.dragovorn.mda.Main;
import com.dragovorn.mda.handler.LockedContainerHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class LockExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        Block block = player.getTargetBlock((Set<Material>) null, 5);

        if (LockedContainerHandler.isLockable(block)) {
            if (Main.getInstance().getLockManager().lock(player, block)) {
                player.sendMessage("Successfully locked block!");
            } else {
                player.sendMessage(Main.getInstance().getLockManager().getWhoLocked(block).getName() + " has already locked that block!");
            }
        } else {
            player.sendMessage("You cannot lock that block!");
        }

        return true;
    }
}
