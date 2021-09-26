package net.galaxycore.onehit.utils;

import net.galaxycore.onehit.OneHit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class SpawnHelper {
    public static Location getRandomSpawn() {
        int spawns = Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("spawn.count"));
        return LocationHelper.getLocation("spawn." + new Random().nextInt(spawns));
    }

    public static boolean isPlayerInASpawn(Player player) {
        return isLocationInASpawn(player.getLocation());
    }

    public static boolean isLocationInASpawn(Location location) {
        boolean inSpawn = false;
        String[] spawns = OneHit.getInstance().getConfigNamespace().get("spawn.shapes").split("\\|");

        for (String spawn : spawns) {
            String[] spawnSplit = spawn.split(" ");

            double x0 = Double.parseDouble(spawnSplit[0]);
            double y0 = Double.parseDouble(spawnSplit[1]);
            double z0 = Double.parseDouble(spawnSplit[2]);
            double x1 = Double.parseDouble(spawnSplit[3]);
            double y1 = Double.parseDouble(spawnSplit[4]);
            double z1 = Double.parseDouble(spawnSplit[5]);
            double x_min = Math.min(x0, x1);
            double y_min = Math.min(y0, y1);
            double z_min = Math.min(z0, z1);
            double x_max = Math.max(x0, x1);
            double y_max = Math.max(y0, y1);
            double z_max = Math.max(z0, z1);

            double x = location.getBlockX();
            double y = location.getBlockY();
            double z = location.getBlockZ();

            inSpawn = inSpawn || (
                    (y >= y_min) &&
                            (y <= y_max) &&
                            (x >= x_min) &&
                            (x <= x_max) &&
                            (z >= z_min) &&
                            (z <= z_max)
            );
        }

        return inSpawn;
    }
}
