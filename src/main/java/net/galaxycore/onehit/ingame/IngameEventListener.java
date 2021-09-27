package net.galaxycore.onehit.ingame;

import net.galaxycore.galaxycorecore.events.ServerTimePassedEvent;
import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.utils.SpawnHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class IngameEventListener implements Listener {
    @EventHandler
    public void onTimePassed(ServerTimePassedEvent event){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(!SpawnHelper.isLocationInASpawn(player.getLocation())) {
                OneHit.getInstance().getIngamePhase().setItems(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();

        if(SpawnHelper.isPlayerInASpawn(damaged)) return;

        damaged.sendTitle("Killed!", "", 20, 40, 20);

        SpawnHelper.reset(damaged);
    }
}
