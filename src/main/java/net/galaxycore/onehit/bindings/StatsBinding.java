package net.galaxycore.onehit.bindings;

import lombok.Getter;
import lombok.SneakyThrows;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.debug.OneHitDebug;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class StatsBinding {
    private final Player player;
    private static final HashMap<UUID, Float> kdCache = new HashMap<>();
    private static final HashMap<UUID, Float> kills = new HashMap<>();
    private static final HashMap<UUID, Float> deaths = new HashMap<>();

    public StatsBinding(Player player) {
        this.player = player;
    }

    @SneakyThrows
    public static void init() {
        OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS `OneHit_stats` (`pid` integer not null, `kills` integer not null default 0, `deaths` integer not null default 0)"
        ).executeUpdate();
    }

    @SneakyThrows
    public static void load(PlayerLoader playerLoader) {
        PreparedStatement statementIsLoaded = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                "SELECT '' FROM `OneHit_stats` WHERE `pid` = ?"
        );
        statementIsLoaded.setInt(1, playerLoader.getId());

        ResultSet resultIsLoaded = statementIsLoaded.executeQuery();
        if (!resultIsLoaded.next()) {
            PreparedStatement statementInsert = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                    "INSERT INTO `OneHit_stats` (`pid`) VALUES (?)"
            );

            statementInsert.setInt(1, playerLoader.getId());
            statementInsert.executeUpdate();
            statementInsert.close();
        }
        resultIsLoaded.close();
        statementIsLoaded.close();

        PreparedStatement getDefaultKD = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                "SELECT `kills`, `deaths` FROM `OneHit_stats` WHERE `pid` = ?"
        );
        getDefaultKD.setInt(1, playerLoader.getId());
        ResultSet resultDefaultKD = getDefaultKD.executeQuery();

        resultDefaultKD.next();
        float k = resultDefaultKD.getInt("kills");
        float d = resultDefaultKD.getInt("deaths");

        kdCache.remove(playerLoader.getUuid());
        kills.remove(playerLoader.getUuid());
        deaths.remove(playerLoader.getUuid());

        if(d == 0)
            kdCache.put(playerLoader.getUuid(), 0F);
        else
            kdCache.put(playerLoader.getUuid(), k/d);

        kills.put(playerLoader.getUuid(), k);
        deaths.put(playerLoader.getUuid(), d);

        resultDefaultKD.close();
        getDefaultKD.close();
    }

    @SneakyThrows
    public void addKill() {
        OneHitDebug.debug(player + " gets a Kill added");

        PreparedStatement statement = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                "UPDATE `OneHit_stats` SET `kills` = `kills` + 1 WHERE `pid` = ?"
        );
        statement.setInt(1, PlayerLoader.load(player).getId());
        statement.executeUpdate();
        statement.close();

        kdUpdate();
    }

    @SneakyThrows
    private void kdUpdate(){
        PreparedStatement getDefaultKD = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                "SELECT `kills`, `deaths` FROM `OneHit_stats` WHERE `pid` = ?"
        );
        getDefaultKD.setInt(1, PlayerLoader.load(player).getId());
        ResultSet resultDefaultKD = getDefaultKD.executeQuery();

        resultDefaultKD.next();
        float k = resultDefaultKD.getInt("kills");
        float d = resultDefaultKD.getInt("deaths");

        kdCache.remove(player.getUniqueId());
        kills.remove(player.getUniqueId());
        deaths.remove(player.getUniqueId());

        if(d == 0)
            kdCache.put(player.getUniqueId(), 0F);
        else
            kdCache.put(player.getUniqueId(), k/d);

        kills.put(player.getUniqueId(), k);
        deaths.put(player.getUniqueId(), k);

        resultDefaultKD.close();
    }

    @SneakyThrows
    public void addDeath() {
        OneHitDebug.debug(player + " gets a Death added");

        PreparedStatement statement = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                "UPDATE `OneHit_stats` SET `kills` = `kills` - 1 WHERE `pid` = ?"
        );
        statement.setInt(1, PlayerLoader.load(player).getId());
        statement.executeUpdate();
        statement.close();

        kdUpdate();
    }

    public float getKD() {
        return kdCache.get(player.getUniqueId());
    }

    public float getKills() {
        return kills.get(player.getUniqueId());
    }

    public float getDeaths() {
        return deaths.get(player.getUniqueId());
    }

    @SneakyThrows
    public void reset() {
        OneHitDebug.debug(player + " gets resetted");

        PreparedStatement statement = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection().prepareStatement(
                "DELETE FROM `OneHit_stats` WHERE `pid` = ?"
        );
        statement.setInt(1, PlayerLoader.load(player).getId());
        statement.executeUpdate();
        statement.close();

        load(PlayerLoader.loadNew(player));
    }
}
