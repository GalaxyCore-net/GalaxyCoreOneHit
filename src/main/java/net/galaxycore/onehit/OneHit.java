package net.galaxycore.onehit;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.onehit.ingame.IngameEventListener;
import net.galaxycore.onehit.ingame.IngamePhase;
import net.galaxycore.onehit.listeners.BaseListeners;
import net.galaxycore.onehit.listeners.JoinListener;
import net.galaxycore.onehit.listeners.MoveListener;
import net.galaxycore.onehit.lobby.LobbyInteractListener;
import net.galaxycore.onehit.lobby.LobbyPhase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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

        // I18N //
        I18N.setDefaultByLang("de_DE", "onehit.settings", "§eEinstellungen");
        I18N.setDefaultByLang("de_DE", "onehit.settings.buy", "§eKaufen: ");
        I18N.setDefaultByLang("de_DE", "onehit.settings.alreadybought", "§eBereits gekauft");
        I18N.setDefaultByLang("de_DE", "onehit.settings.booster.lore", "Schleudert dich für 5 min nach vorne\n§7(150 Coins)");
        I18N.setDefaultByLang("de_DE", "onehit.settings.booster.bought", "§eDu hast den Booster für 5 min erhalten!");
        I18N.setDefaultByLang("de_DE", "onehit.booster", "§cBooster");
        I18N.setDefaultByLang("de_DE", "onehit.sword", "§cSchwert");
        I18N.setDefaultByLang("de_DE", "onehit.bow", "§cBogen");
        I18N.setDefaultByLang("de_DE", "onehit.arrow", "§cPfeil");

        I18N.setDefaultByLang("en_GB", "onehit.settings", "§eSettings");
        I18N.setDefaultByLang("en_GB", "onehit.settings.buy", "§eBuy: ");
        I18N.setDefaultByLang("en_GB", "onehit.settings.alreadybought", "§eAlready bought");
        I18N.setDefaultByLang("en_GB", "onehit.settings.booster.lore", "Throws yourself forward for 5 min\n§7(150 Coins)");
        I18N.setDefaultByLang("en_GB", "onehit.settings.booster.bought", "§eYou bought the Booster for five minutes!");
        I18N.setDefaultByLang("en_GB", "onehit.booster", "§cBooster");
        I18N.setDefaultByLang("en_GB", "onehit.sword", "§cSword");
        I18N.setDefaultByLang("en_GB", "onehit.bow", "§cBow");
        I18N.setDefaultByLang("en_GB", "onehit.arrow", "§cArrow");

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
    }
}
