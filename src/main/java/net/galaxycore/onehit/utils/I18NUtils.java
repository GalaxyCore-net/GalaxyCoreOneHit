package net.galaxycore.onehit.utils;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.galaxycorecore.permissions.LuckPermsApiWrapper;
import net.galaxycore.galaxycorecore.utils.StringUtils;
import net.galaxycore.onehit.listeners.MessageSetLoader;
import org.bukkit.entity.Player;

public class I18NUtils {

    public static String get(Player player, String key) {
        String i18nval = I18N.getByPlayer(player, "onehit." + MessageSetLoader.get(player) + "." + key);
        return StringUtils.replaceRelevant(i18nval, new LuckPermsApiWrapper(player));
    }
}
