package net.galaxycore.onehit.lobby;

import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.utils.SpawnHelper;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbyInteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) return;

        if(event.getItem() == null) return;
        if (event.getItem().getType() == Material.FIRE_CHARGE && OneHit.getInstance().getLobbyPhase().getBoosters().containsKey(event.getPlayer().getUniqueId())) {

            event.getPlayer().setVelocity(event.getPlayer().getLocation().getDirection().setY(0).multiply(4));
            OneHit.getInstance().getIngamePhase().setItems(event.getPlayer());
        }

        if(!SpawnHelper.isPlayerInASpawn(event.getPlayer())) return;
        if(event.getItem().getType() == Material.NETHER_STAR){
            new SettingsMenu(event.getPlayer()).open();
        }
    }
}
