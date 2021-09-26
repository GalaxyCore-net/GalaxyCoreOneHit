package net.galaxycore.onehit;

import lombok.Getter;
import net.galaxycore.galaxycorecore.GalaxyCoreCore;
import net.galaxycore.galaxycorecore.configuration.ConfigNamespace;
import net.galaxycore.onehit.listeners.BaseListeners;
import net.galaxycore.onehit.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class OneHit extends JavaPlugin {
    @Getter
    private static OneHit instance;

    private GalaxyCoreCore core;
    private ConfigNamespace configNamespace;

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

        // LISTENERS//
        Bukkit.getPluginManager().registerEvents(new BaseListeners(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }
}
