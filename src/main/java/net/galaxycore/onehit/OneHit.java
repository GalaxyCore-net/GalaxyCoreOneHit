package net.galaxycore.onehit;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.onehit.debug.DebugCommand;
import net.galaxycore.onehit.ingame.IngameEventListener;
import net.galaxycore.onehit.ingame.IngamePhase;
import net.galaxycore.onehit.listeners.BaseListeners;
import net.galaxycore.onehit.listeners.JoinListener;
import net.galaxycore.onehit.listeners.MessageSetLoader;
import net.galaxycore.onehit.listeners.MoveListener;
import net.galaxycore.onehit.lobby.LobbyInteractListener;
import net.galaxycore.onehit.lobby.LobbyPhase;
import org.bukkit.Bukkit;
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

        // I18N MESSAGE SET LOADER //
        Bukkit.getPluginManager().registerEvents(new MessageSetLoader(), this);

        // I18N //
        // Messageset 1
        I18N.setDefaultByLang("de_DE", "onehit.0.settings", "§eEinstellungen");
        I18N.setDefaultByLang("de_DE", "onehit.0.settings.buy", "§eKaufen: ");
        I18N.setDefaultByLang("de_DE", "onehit.0.settings.alreadybought", "§eBereits gekauft");
        I18N.setDefaultByLang("de_DE", "onehit.0.settings.booster.lore", "Schleudert dich für 5 min nach vorne");
        I18N.setDefaultByLang("de_DE", "onehit.0.settings.booster.bought", "§eDu hast den Booster für 5 min erhalten!");
        I18N.setDefaultByLang("de_DE", "onehit.0.settings.booster.price", "§7150 Coins");
        I18N.setDefaultByLang("de_DE", "onehit.0.booster", "§cBooster");
        I18N.setDefaultByLang("de_DE", "onehit.0.sword", "§cSchwert");
        I18N.setDefaultByLang("de_DE", "onehit.0.bow", "§cBogen");
        I18N.setDefaultByLang("de_DE", "onehit.0.arrow", "§cPfeil");
        I18N.setDefaultByLang("de_DE", "onehit.0.killed", "§l§c✖ §r§4Gestorben");
        I18N.setDefaultByLang("de_DE", "onehit.0.killed.sub", "%rank_prefix%%player% §8| §c-5 Coins");
        I18N.setDefaultByLang("de_DE", "onehit.0.killedself", "%rank_prefix%%player% §9hat sich selber mit dem Bogen abgeschossen. Das ist belastend.");
        I18N.setDefaultByLang("de_DE", "onehit.0.wonfight", "§l§a✓ §r§4+5 Coins");
        I18N.setDefaultByLang("de_DE", "onehit.0.wonfight.sub", "%rank_prefix%%player%");

        I18N.setDefaultByLang("en_GB", "onehit.0.settings", "§eSettings");
        I18N.setDefaultByLang("en_GB", "onehit.0.settings.buy", "§eBuy: ");
        I18N.setDefaultByLang("en_GB", "onehit.0.settings.alreadybought", "§eAlready bought");
        I18N.setDefaultByLang("en_GB", "onehit.0.settings.booster.lore", "Throws yourself forward for 5 min\n§7(150 Coins)");
        I18N.setDefaultByLang("en_GB", "onehit.0.settings.booster.bought", "§eYou bought the Booster for five minutes!");
        I18N.setDefaultByLang("en_GB", "onehit.0.settings.booster.price", "§7150 Coins");
        I18N.setDefaultByLang("en_GB", "onehit.0.booster", "§cBooster");
        I18N.setDefaultByLang("en_GB", "onehit.0.sword", "§cSword");
        I18N.setDefaultByLang("en_GB", "onehit.0.bow", "§cBow");
        I18N.setDefaultByLang("en_GB", "onehit.0.arrow", "§cArrow");
        I18N.setDefaultByLang("en_GB", "onehit.0.killed", "§l§c✖ §r§4You Died");
        I18N.setDefaultByLang("en_GB", "onehit.0.killed.sub", "%rank_prefix%%player% §8| §c-1 Coin");
        I18N.setDefaultByLang("en_GB", "onehit.0.killedself", "%rank_prefix%%player% §9tried to kill himself with a bow. That's hilarious!");
        I18N.setDefaultByLang("en_GB", "onehit.0.wonfight", "§l§a✓ §r§4+5 Coins");
        I18N.setDefaultByLang("en_GB", "onehit.0.wonfight.sub", "%rank_prefix%%player%");

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

        // DEBUG //
        Objects.requireNonNull(getCommand("debug")).setExecutor(new DebugCommand());
        Objects.requireNonNull(getCommand("debug")).setTabCompleter(new DebugCommand());
    }
}
