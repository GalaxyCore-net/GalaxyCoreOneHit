package net.galaxycore.onehit.ingame;

import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.utils.SpawnHelper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class IngameEventListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if(SpawnHelper.isLocationInASpawn(event.getFrom()) && !SpawnHelper.isLocationInASpawn(event.getTo())) {
            OneHit.getInstance().getIngamePhase().setItems(player);
        }
    }
}
