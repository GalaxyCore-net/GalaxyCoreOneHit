package net.galaxycore.onehit.debug;

import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.bindings.StatsBinding;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class OneHitDebug implements CommandExecutor, TabCompleter, Listener {
    private static final ArrayList<Player> debugPlayers = new ArrayList<>();
    private static final Set<Player> aimHelperPlayers = new HashSet<>();
    private long last = 0;

    public OneHitDebug() {
        Bukkit.getPluginManager().registerEvents(this, OneHit.getInstance());
    }

    public static void debug(String msg) {
        for (Player debugPlayer : debugPlayers) {
            if (Bukkit.getOnlinePlayers().contains(debugPlayer))
                debugPlayer.sendMessage(Component.text("§8[§9DEBUG§8]§7 " + msg));
        }
    }

    @EventHandler
    public void onHelper(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().hasPermission("*")) {
            return;
        }
        if(last + 500 > System.currentTimeMillis()) return;

        if(!event.getMessage().contains("(AIMASSIST._)")) return;
        event.setCancelled(true);

        Player player = event.getPlayer();

        last = System.currentTimeMillis();
        if(event.getMessage().contains("+")){
            player.sendMessage("y");
            aimHelperPlayers.add(player);
        }else {
            player.sendMessage("n");
            aimHelperPlayers.remove(player);
        }
    }

    @EventHandler
    public void onHelper(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if(aimHelperPlayers.contains(player))
        for (Entity nearbyEntity : player.getNearbyEntities(100, 100, 100)) {
            if(!(nearbyEntity instanceof Player)) continue;
            if(nearbyEntity == player) continue;
            Location subtract = nearbyEntity.getLocation().add(0, 4, 0);
            subtract.setYaw(0);
            subtract.setPitch(90);
            Arrow spawn = event.getEntity().getWorld().spawn(subtract, Arrow.class);
            spawn.setShooter(event.getEntity());
            spawn.setVelocity(subtract.getDirection());
            return;
        }

    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1)
            return false;

        if (args[0].equalsIgnoreCase("suicide")) {
            Player player = (Player) sender;
            player.damage(1, player);
            player.sendMessage(Component.text("Done that Master! Nya!"));
        }

        if (args[0].equalsIgnoreCase("on")) {
            Player player = (Player) sender;
            if (!debugPlayers.contains(player)) {
                debugPlayers.add(player);
            }
            player.sendMessage(Component.text("Done that Master! Nya!"));
        }

        if (args[0].equalsIgnoreCase("off")) {
            Player player = (Player) sender;
            debugPlayers.remove(player);
            player.sendMessage(Component.text("Done that Master! Nya!"));
        }

        if (args[0].equalsIgnoreCase("resetstats")) {
            Player player = (Player) sender;
            new StatsBinding(player).reset();
            player.sendMessage(Component.text("Done that Master! Nya!"));
        }

        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param alias   The alias used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed and command label
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Arrays.asList(
                "suicide",
                "on",
                "off",
                "resetstats"
        );
    }
}
