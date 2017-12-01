package com.dragovorn.mda.command;

import com.dragovorn.mda.Main;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

import static com.dragovorn.mda.handler.LockedContainerHandler.getAdjacentLocked;
import static com.dragovorn.mda.handler.LockedContainerHandler.isLockable;
import static com.dragovorn.mda.util.ChatHelper.colourize;

public class LockExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        Block target = player.getTargetBlock((Set<Material>) null, 5);

        if (isLockable(target)) {
            Block lock = getAdjacentLocked(target);

            if (lock == null) {
                lock = target;
            }

            if (Main.getInstance().getLockManager().lock(player, lock)) {
                player.sendMessage(colourize("&aSuccessfully locked block!"));
            } else {
                OfflinePlayer owner = Main.getInstance().getLockManager().getWhoLocked(lock);

                if (owner.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(colourize("&cYou've already locked that block!"));
                } else {
                    player.sendMessage(colourize("&e" + owner.getName() + " &chas already locked that block!"));
                }
            }
        } else {
            player.sendMessage(colourize("&cYou cannot lock that block!"));
        }

        return true;
    }
}
