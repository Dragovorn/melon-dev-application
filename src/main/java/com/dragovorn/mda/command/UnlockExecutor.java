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
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) { // Player only command
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        Block target = player.getTargetBlock((Set<Material>) null, 5);

        if (isLockable(target)) { // Make sure our target can be locked
            // Get adjacent blocks, used for pre-existing double chests or doors
            Block extra = getAdjacent(target, true);

            // Get the owner to make sure that the unlocking player is the owner
            OfflinePlayer owner = Main.getInstance().getLockManager().getWhoLocked(target);

            // Actually unlock the block
            if (owner.getUniqueId().equals(player.getUniqueId()) || player.hasPermission("lock.bypass")) {
                unlock(target, extra, player);
            } else {
                player.sendMessage(colourize("&cYou cannot unlock that block because it's owner is: &e" + owner.getName()));
            }
        } else {
            player.sendMessage(colourize("&cYou cannot unlock that block!"));
        }

        return true;
    }

    private void unlock(Block block, Block extra, Player player) {
        // Attempt to unlock the block
        if (Main.getInstance().getLockManager().unlock(block)) {
            // Unlock extra blocks (for doors & pre-existing double chests)
            if (extra != null) {
                Main.getInstance().getLockManager().unlock(extra);
            }

            player.sendMessage(colourize("&aYou unlocked this block!"));
        } else {
            player.sendMessage(colourize("&cThat block isn't locked!"));
        }
    }
}
