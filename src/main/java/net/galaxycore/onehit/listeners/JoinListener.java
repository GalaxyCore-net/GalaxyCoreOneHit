package net.galaxycore.onehit.listeners;

import net.galaxycore.onehit.utils.SpawnHelper;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.getPlayer().setGameMode(GameMode.ADVENTURE);

        event.getPlayer().setExp(0);
        event.getPlayer().setLevel(0);

        event.getPlayer().teleport(SpawnHelper.getRandomSpawn());
    }
}
