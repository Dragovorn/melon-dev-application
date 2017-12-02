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

public class LockExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) { // Player only command
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        // Variables for my sanity
        Player player = (Player) sender;

        Block target = player.getTargetBlock((Set<Material>) null, 5);

        if (isLockable(target)) { // Make sure our target can be locked
            // Get adjacent blocks, used for pre-existing double chests or doors
            Block extra = getAdjacent(target, false);

            // Attempt to lock
            if (Main.getInstance().getLockManager().lock(player, target)) {
                // Lock extra blocks (for doors & pre-existing double chests)
                if (extra != null) {
                    Main.getInstance().getLockManager().lock(player, extra);
                }
                player.sendMessage(colourize("&aSuccessfully locked block!"));
            } else {
                // Get owner of the lock
                OfflinePlayer owner = Main.getInstance().getLockManager().getWhoLocked(target);

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
