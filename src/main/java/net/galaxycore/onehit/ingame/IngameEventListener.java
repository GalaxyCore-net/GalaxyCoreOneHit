package net.galaxycore.onehit.ingame;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.events.ServerTimePassedEvent;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.bindings.CoinsBinding;
import net.galaxycore.onehit.bindings.StatsBinding;
import net.galaxycore.onehit.listeners.MessageSetLoader;
import net.galaxycore.onehit.utils.I18NUtils;
import net.galaxycore.onehit.utils.SpawnHelper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

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
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Component.text(StringUtils.replaceRelevant(I18N.getByPlayer(player, "onehit." + MessageSetLoader.get(player) + ".killedself"), new LuckPermsApiWrapper(damaged)))));
                event.getDamager().remove();
                return;
            }
            
            event.getDamager().remove();

            damaged.sendTitle(
                    I18NUtils.getRF(damaged, "killed", ((Player) Objects.requireNonNull(((Arrow) event.getDamager())).getShooter())),
                    I18NUtils.getRF(damaged, "killed.sub", ((Player) Objects.requireNonNull(((Arrow) event.getDamager())).getShooter())),
                    20,
                    40,
                    20
            );

            ((Player) Objects.requireNonNull(((Arrow) event.getDamager()).getShooter())).sendTitle(
                    I18NUtils.getRF((Player) ((Arrow) event.getDamager()).getShooter(), "wonfight", damaged),
                    I18NUtils.getRF((Player) ((Arrow) event.getDamager()).getShooter(), "wonfight.sub", damaged),
                    20,
                    40,
                    20
            );

            damaged.playSound(Sound.sound(Key.key("minecraft", "entity.player.death"), Sound.Source.MASTER, 1f, 1f));
            ((Player) Objects.requireNonNull(((Arrow) event.getDamager()).getShooter())).playSound(Sound.sound(Key.key("minecraft", "block.note_block.pling"), Sound.Source.MASTER, 1f, 2f));

            spawnArrow(((Player) Objects.requireNonNull(((Arrow) event.getDamager()).getShooter())));
            registerPlayerDead(((Player) Objects.requireNonNull(((Arrow) event.getDamager()).getShooter())), damaged);

            SpawnHelper.reset(damaged);
        } else if (killerType == EntityType.PLAYER) {
            if (SpawnHelper.isPlayerInASpawn((Player) event.getDamager())) return;
            damaged.sendTitle(
                    I18NUtils.getRF(damaged, "killed", ((Player) event.getDamager())),
                    I18NUtils.getRF(damaged, "killed.sub", ((Player) event.getDamager())),
                    20,
                    40,
                    20
            );

            ((Player) event.getDamager()).sendTitle(
                    I18NUtils.getRF((Player) event.getDamager(), "wonfight", damaged),
                    I18NUtils.getRF((Player) event.getDamager(), "wonfight.sub", damaged),
                    20,
                    40,
                    20
            );

            damaged.playSound(Sound.sound(Key.key("minecraft", "entity.player.death"), Sound.Source.MASTER, 1f, 1f));
            event.getDamager().playSound(Sound.sound(Key.key("minecraft", "block.note_block.pling"), Sound.Source.MASTER, 1f, 2f));

            spawnArrow((Player) event.getDamager());

            registerPlayerDead((Player) event.getDamager(), damaged);

            SpawnHelper.reset(damaged);
        }
    }

    private void registerPlayerDead(Player damager, Player damaged) {
        new StatsBinding(damager).addKill();
        new StatsBinding(damaged).addDeath();

        StreakManager.registerKill(damager);
        StreakManager.registerDeath(damaged);

        new CoinsBinding(damager).increase(Long.parseLong(OneHit.getInstance().getConfigNamespace().get("kill_coins_plus")));
        new CoinsBinding(damaged).decrease(Long.parseLong(OneHit.getInstance().getConfigNamespace().get("death_coins_minus")), "PlayerDeath");
    }

    private void spawnArrow(Player player) {
        Inventory inventory = player.getInventory();
        if (inventory.getItem(8) == null) {
            OneHit.getInstance().getIngamePhase().setItemsWithoutHesitaiton(player);
            return;
        }

        Objects.requireNonNull(inventory.getItem(8)).setAmount(Objects.requireNonNull(inventory.getItem(8)).getAmount() < 16 ? Objects.requireNonNull(inventory.getItem(8)).getAmount() + 1 : 16);
    }
}
