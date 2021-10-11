package net.galaxycore.onehit.listeners;

import lombok.Getter;
import lombok.SneakyThrows;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.onehit.OneHit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class MessageSetLoader implements Listener {
    private static Connection connection;
    private static final HashMap<UUID, Integer> messageSets = new HashMap<>();

    @SneakyThrows
    public MessageSetLoader() {
        connection = OneHit.getInstance().getCore().getDatabaseConfiguration().getConnection();

        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `OneHit_MessageSetData` (`playerid` INTEGER NOT NULL, `messageset` INTEGER NOT NULL DEFAULT 0);");
        statement.executeUpdate();
        statement.close();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        reloadPlayer(event.getPlayer());
    }

    @SneakyThrows
    public static void reloadPlayer(Player player){
        PreparedStatement ps = connection.prepareStatement("SELECT `messageset` FROM `OneHit_MessageSetData` WHERE `playerid` = ?");
        ps.setInt(1, PlayerLoader.load(player).getId());
        ResultSet rs = ps.executeQuery();

        if(!rs.next()){
            rs.close();

            get(player);
            return;
        }

        messageSets.remove(player.getUniqueId());
        messageSets.put(player.getUniqueId(), rs.getInt("messageset"));

        rs.close();
        ps.close();
}

    @SneakyThrows
    public static void set(Player player, int num) {
        PreparedStatement ps = connection.prepareStatement("UPDATE `OneHit_MessageSetData` SET `messageset` = ? WHERE `playerid` = ?");
        ps.setInt(1, num);
        ps.setInt(2, PlayerLoader.load(player).getId());
        ps.executeUpdate();
        ps.close();
    }

    public static int get(Player player) {
        messageSets.computeIfAbsent(player.getUniqueId(), uuid -> {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO `OneHit_MessageSetData` (`playerid`, `messageset`) VALUES (?, ?)");
                ps.setInt(1, PlayerLoader.load(player).getId());
                ps.setInt(2, 0);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return 0;
        });
        return messageSets.get(player.getUniqueId());
    }
}
