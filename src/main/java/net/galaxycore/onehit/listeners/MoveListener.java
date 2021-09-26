package net.galaxycore.onehit.listeners;

import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.utils.SpawnHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(SpawnHelper.isPlayerInASpawn(event.getPlayer())){
            OneHit.getInstance().getLobbyPhase().setItems(event.getPlayer());
        }
    }
}
