package net.galaxycore.onehit.bindings;

import lombok.Getter;
import net.galaxycore.onehit.debug.OneHitDebug;
import org.bukkit.entity.Player;

@Getter
public class StatsBinding {
    private final Player player;

    public StatsBinding(Player player) {
        this.player = player;
    }

    public void addKill(){
        OneHitDebug.debug(player + " gets a Kill added");
    }

    public void addDeath(){
        OneHitDebug.debug(player + " gets a Death added");
    }

    public float getKD(){return 0;}

    public void reset(){
        OneHitDebug.debug(player + " gets resetted");
    }
}
