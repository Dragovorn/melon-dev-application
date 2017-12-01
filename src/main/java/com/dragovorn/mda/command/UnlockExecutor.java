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

import static com.dragovorn.mda.helper.ChatHelper.colourize;

public class UnlockExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        Block target = player.getTargetBlock((Set<Material>) null, 5);

        if (LockedContainerHandler.isLockable(target)) {
            Block unlock = LockedContainerHandler.getAdjacentLocked(target);

            if (unlock == null) {
                unlock = target;
            }

            if (Main.getInstance().getLockManager().unlock(unlock)) {
                player.sendMessage(colourize("&aYou unlocked this block"));
            } else {
                player.sendMessage(colourize("&cYou cannot unlock this block, as it is locked by: &a" + Main.getInstance().getLockManager().getWhoLocked(unlock).getName()));
            }
        }

        return true;
    }
}
