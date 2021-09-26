package net.galaxycore.onehit.utils;

import net.galaxycore.onehit.OneHit;
import org.bukkit.Location;

import java.util.Random;

public class SpawnHelper {
    public static Location getRandomSpawn(){
        int spawns = Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("spawn.count"));
        return LocationHelper.getLocation("spawn." + new Random().nextInt(spawns));
    }
}
