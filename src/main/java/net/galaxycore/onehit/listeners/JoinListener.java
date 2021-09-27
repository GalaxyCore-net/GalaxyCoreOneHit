package net.galaxycore.onehit.listeners;

import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.utils.SpawnHelper;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.getPlayer().setGameMode(GameMode.ADVENTURE);

        event.getPlayer().setExp(0);
        event.getPlayer().setLevel(0);

        Objects.requireNonNull(event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(2);

        SpawnHelper.reset(event.getPlayer());
    }
}
