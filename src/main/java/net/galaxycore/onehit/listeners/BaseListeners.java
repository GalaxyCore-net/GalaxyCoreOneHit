package net.galaxycore.onehit.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import net.galaxycore.onehit.OneHit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class BaseListeners implements Listener {
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySummon(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHandChange(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupArrow(PlayerPickupArrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onArmorStandEdit(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFlowerEdit(PlayerFlowerPotManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSendArrow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            new ArrowDestroyJob(event.getProjectile()).runTaskLater(OneHit.getInstance(), 10 * 20L);
        }
    }

    @EventHandler
    public void onPlayerDead(PlayerDeathEvent event) {
        event.setCancelled(true);
    }

    static class ArrowDestroyJob extends BukkitRunnable {

        private final Entity projectile;

        ArrowDestroyJob(Entity projectile) {
            this.projectile = projectile;
        }

        @Override
        public void run() {
            projectile.remove();
        }
    }
}
