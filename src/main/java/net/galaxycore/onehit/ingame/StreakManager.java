package net.galaxycore.onehit.ingame;

import lombok.Getter;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.galaxycore.onehit.debug.OneHitDebug;
import net.galaxycore.onehit.utils.I18NUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class StreakManager implements Listener {
    private static final HashMap<Player, AtomicInteger> streaks = new HashMap<>();

    public static void registerKill(Player player) {
        int streak = streaks.get(player).incrementAndGet();
        player.setExp(0);
        player.setLevel(streak);

        if (streak % 3 == 0)
            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                onlinePlayer.sendMessage(Component.text(String.format(StringUtils.replaceRelevant(I18NUtils.get(player, "streak"), new LuckPermsApiWrapper(player)), streak)));


        OneHitDebug.debug(player.getName() + " now has a streak of " + streak);
    }

    public static void registerDeath(Player player) {
        streaks.remove(player);
        streaks.put(player, new AtomicInteger());
        player.setExp(0);
        player.setLevel(0);
        OneHitDebug.debug(player.getName() + " lost their streak");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        streaks.remove(event.getPlayer());
        streaks.put(event.getPlayer(), new AtomicInteger());
        event.getPlayer().setExp(0);
        event.getPlayer().setLevel(0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        streaks.remove(event.getPlayer());
    }

}
