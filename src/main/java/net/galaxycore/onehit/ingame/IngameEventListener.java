package net.galaxycore.onehit.ingame;

import net.galaxycore.galaxycorecore.chattools.ChatManager;
import net.galaxycore.galaxycorecore.events.ServerTimePassedEvent;
import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.utils.I18NUtils;
import net.galaxycore.onehit.utils.SpawnHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class IngameEventListener implements Listener {
    @EventHandler
    public void onTimePassed(ServerTimePassedEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!SpawnHelper.isLocationInASpawn(player.getLocation())) {
                OneHit.getInstance().getIngamePhase().setItems(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();

        if (SpawnHelper.isPlayerInASpawn(damaged)) return;

        EntityType killerType = event.getDamager().getType();

        if (killerType == EntityType.ARROW) {
            if (((Arrow) event.getDamager()).getShooter() == damaged) {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Component.text(I18NUtils.get(player, "killedself"))));
                event.getDamager().remove();
                return;
            }

            damaged.sendTitle(
                    I18NUtils.get((Player) ((Arrow) event.getDamager()).getShooter(), "killed"),
                    I18NUtils.get((Player) ((Arrow) event.getDamager()).getShooter(), "killed.sub"),
                    20,
                    40,
                    20
            );

            SpawnHelper.reset(damaged);
        } else if (killerType == EntityType.PLAYER) {
            damaged.sendTitle(
                    I18NUtils.get((Player) event.getDamager(), "killed"),
                    I18NUtils.get((Player) event.getDamager(), "killed.sub"),
                    20,
                    40,
                    20
            );

            SpawnHelper.reset(damaged);
        }
    }
}
