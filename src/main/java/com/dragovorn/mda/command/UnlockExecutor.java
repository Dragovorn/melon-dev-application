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

import static com.dragovorn.mda.util.GeneralHelpers.*;

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
            Block extra = getAdjacent(target, true);

            OfflinePlayer owner = Main.getInstance().getLockManager().getWhoLocked(target);

            if (owner.getUniqueId().equals(player.getUniqueId())) {
                unlock(target, extra, player);
            } else {
                if (player.hasPermission("lock.bypass")) {
                    unlock(target, extra, player);
                } else {
                    player.sendMessage(colourize("&cYou cannot unlock that block because it's owner is: &e" + owner.getName()));
                }
            }
        } else {
            player.sendMessage(colourize("&cYou cannot unlock that block!"));
        }

        return true;
    }

    private void unlock(Block block, Block extra, Player player) {
        if (Main.getInstance().getLockManager().unlock(block)) {
            if (extra != null) {
                Main.getInstance().getLockManager().unlock(extra);
            }

            player.sendMessage(colourize("&aYou unlocked this block!"));
        } else {
            player.sendMessage(colourize("&cThat block isn't locked!"));
        }
    }
}
