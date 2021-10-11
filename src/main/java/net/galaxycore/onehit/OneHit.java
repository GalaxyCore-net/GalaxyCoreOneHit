package net.galaxycore.onehit;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.scoreboards.ScoreBoardController;
import net.galaxycore.onehit.bindings.ScoreboardCallback;
import net.galaxycore.onehit.bindings.StatsBinding;
import net.galaxycore.onehit.debug.OneHitDebug;
import net.galaxycore.onehit.ingame.IngameEventListener;
import net.galaxycore.onehit.ingame.IngamePhase;
import net.galaxycore.onehit.ingame.StreakManager;
import net.galaxycore.onehit.listeners.BaseListeners;
import net.galaxycore.onehit.listeners.JoinListener;
import net.galaxycore.onehit.listeners.MessageSetLoader;
import net.galaxycore.onehit.listeners.MoveListener;
import net.galaxycore.onehit.lobby.LobbyInteractListener;
import net.galaxycore.onehit.lobby.LobbyPhase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class OneHit extends JavaPlugin {
    @Getter
    private static OneHit instance;

    private GalaxyCoreCore core;
    private ConfigNamespace configNamespace;

    private LobbyPhase lobbyPhase;
    private IngamePhase ingamePhase;

    @Override
    public void onEnable() {
        OneHit.instance = this;

        // CORE //
        core = Bukkit.getServicesManager().load(GalaxyCoreCore.class);
        getLogger().info("Using Core: " + core);

        // CONFIG //
        configNamespace = core.getDatabaseConfiguration().getNamespace("OneHit");
        configNamespace.setDefault("spawn.count", "1");
        configNamespace.setDefault("spawn.0", "world -9 97 90 180");
        configNamespace.setDefault("spawn.shapes", "-11 97 88 -7 100 92");
        configNamespace.setDefault("price.booster", "150");
        configNamespace.setDefault("price.messageset.0", "1500");
        configNamespace.setDefault("price.messageset.1", "1500");
        configNamespace.setDefault("price.messageset.2", "1500");
        configNamespace.setDefault("death_coins_minus", "1");
        configNamespace.setDefault("kill_coins_plus", "5");

        // I18N MESSAGE SET LOADER //
        Bukkit.getPluginManager().registerEvents(new MessageSetLoader(), this);

        // I18N //
        for (int i = 0; i < 4; i++) {
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings", "§eEinstellungen");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.buy", "§eKaufen: ");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.alreadybought", "§eBereits gekauft");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.coins", "Coins");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.setchosen", "§eSet Ausgewählt");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.booster.lore", "Schleudert dich für 5 min nach vorne");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.booster.bought", "§eDu hast den Booster für 5 min erhalten!");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.booster.price", "§7150 Coins");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.messageset.0.name", "Standart-Nachrichten");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.messageset.1.name", "Spezial-Nachrichten 1");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.messageset.2.name", "Spezial-Nachrichten 2");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".settings.messageset.3.name", "Spezial-Team-Nachrichten");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".booster", "§cBooster");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".sword", "§cSchwert");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".bow", "§cBogen");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".arrow", "§cPfeil");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".killed", "§l§c✖ §r§4Gestorben");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".killed.sub", "%rank_prefix%%player% §8| §c-5 Coins");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".killedself", "%rank_prefix%%player% §9hat sich selber mit dem Bogen abgeschossen. Das ist belastend.");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".wonfight", "§l§a✓ §r§4+5 Coins");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".wonfight.sub", "%rank_prefix%%player%");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.kills", "Kills");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.kills.color", "§e");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.deaths", "Tode");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.deaths.color", "§e");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.kd", "KD");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.kd.color", "§e");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.coins", "Coins");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.coins.color", "§e");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.sub", "Teamspeak-Server");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".score.sub.value", "galaxycore.net");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".nomoney", "§cDu hast nicht genügend Geld");
            I18N.setDefaultByLang("de_DE", "onehit." + i + ".streak", "§c%player% hat nun einen Streak von %d!");

            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings", "§eSettings");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.buy", "§eBuy: ");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.coins", "Coins");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.alreadybought", "§eAlready bought");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.setchosen", "§eSet Chosen");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.booster.lore", "Throws yourself forward for 5 min\n§7(150 Coins)");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.booster.bought", "§eYou bought the Booster for five minutes!");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.booster.price", "§7150 Coins");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.messageset.0.name", "Normal Messages");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.messageset.1.name", "Special Messages 1");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.messageset.2.name", "Special Messages 2");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".settings.messageset.3.name", "Special Team Messages");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".booster", "§cBooster");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".sword", "§cSword");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".bow", "§cBow");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".arrow", "§cArrow");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".killed", "§l§c✖ §r§4You Died");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".killed.sub", "%rank_prefix%%player% §8| §c-1 Coin");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".killedself", "%rank_prefix%%player% §9tried to kill himself with a bow. That's hilarious!");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".wonfight", "§l§a✓ §r§4+5 Coins");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".wonfight.sub", "%rank_prefix%%player%");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.kills", "Kills");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.kills.color", "§e");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.deaths", "Deaths");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.deaths.color", "§e");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.kd", "KD");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.kd.color", "§e");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.coins", "Coins");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.coins.color", "§e");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.sub", "Teamspeak-Server");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".score.sub.value", "galaxycore.net");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".nomoney", "§cYou don't have enough money");
            I18N.setDefaultByLang("en_GB", "onehit." + i + ".streak", "§c%player% now has a streak of %d");
        }
        // LISTENERS//
        Bukkit.getPluginManager().registerEvents(new BaseListeners(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(), this);

        // LOBBY PHASE //
        lobbyPhase = new LobbyPhase();
        Bukkit.getPluginManager().registerEvents(new LobbyInteractListener(), this);

        // INGAME PHASE //
        ingamePhase = new IngamePhase();
        Bukkit.getPluginManager().registerEvents(new IngameEventListener(), this);

        // STREAKS //
        Bukkit.getPluginManager().registerEvents(new StreakManager(), this);

        // STATS //
        StatsBinding.init();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                StatsBinding.load(PlayerLoader.load(event.getPlayer()));
            }
        }, this);

        // SCOREBOARD //
        ScoreBoardController.setScoreBoardCallback(new ScoreboardCallback());

        // DEBUG //
        Objects.requireNonNull(getCommand("ohdb")).setExecutor(new OneHitDebug());
        Objects.requireNonNull(getCommand("ohdb")).setTabCompleter(new OneHitDebug());
    }
}
