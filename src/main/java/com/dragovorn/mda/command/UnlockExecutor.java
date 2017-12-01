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

public class UnlockExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        Block target = player.getTargetBlock((Set<Material>) null, 5);

        if (isLockable(target)) {
            Block block = getAdjacentLocked(target);

            if (block == null) {
                block = target;
            }

            OfflinePlayer owner = Main.getInstance().getLockManager().getWhoLocked(block);

            if (owner.getUniqueId().equals(player.getUniqueId())) {
                unlock(block, player);
            } else {
                if (player.hasPermission("lock.bypass")) {
                    unlock(block, player);
                } else {
                    player.sendMessage(colourize("&cYou cannot unlock that block because it's owner is: &e" + owner.getName()));
                }
            }
        } else {
            player.sendMessage(colourize("&cYou cannot unlock that block!"));
        }

        return true;
    }

    private void unlock(Block block, Player player) {
        if (Main.getInstance().getLockManager().unlock(block)) {
            player.sendMessage(colourize("&aYou unlocked this block!"));
        } else {
            player.sendMessage(colourize("&cThat block isn't locked!"));
        }
    }
}
