package net.galaxycore.onehit.bindings;

import net.galaxycore.galaxycorecore.scoreboards.IScoreBoardCallback;
import net.galaxycore.onehit.utils.I18NUtils;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class ScoreboardCallback implements IScoreBoardCallback {

    @Override
    public String[] getKV(Player player, int i) {
        String[] kv = new String[3];

        StatsBinding statsBinding;

        switch (i) {
            case 0:
                kv[0] = I18NUtils.get(player, "score.kills");
                statsBinding = new StatsBinding(player);
                kv[2] = String.valueOf((int) statsBinding.getKills());
                break;
            case 1:
                kv[0] = I18NUtils.get(player, "score.deaths");
                statsBinding = new StatsBinding(player);
                kv[2] = String.valueOf((int) statsBinding.getDeaths());
                break;
            case 2:
                kv[0] = I18NUtils.get(player, "score.kd");
                statsBinding = new StatsBinding(player);
                kv[2] = new DecimalFormat("#.##").format(statsBinding.getKD());
                break;
            case 3:
                kv[0] = I18NUtils.get(player, "score.coins");
                CoinsBinding coinsBinding = new CoinsBinding(player);
                kv[2] = String.valueOf(coinsBinding.getCoins());
                break;
            case 4:
                kv[0] = I18NUtils.get(player, "score.sub");
                kv[2] = I18NUtils.get(player, "score.sub.value");
                break;
        }

        kv[1] = "";

        return kv;
    }

    @Override
    public String getTitle(Player player) {
        return "§5Galaxy§6Core";
    }

    @Override
    public boolean active(Player player) {
        return true;
    }
}
