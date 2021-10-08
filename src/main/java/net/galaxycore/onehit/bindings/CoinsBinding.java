package net.galaxycore.onehit.bindings;

import lombok.Getter;
import net.galaxycore.galaxycorecore.coins.CoinDAO;
import net.galaxycore.galaxycorecore.coins.PlayerTransactionError;
import net.galaxycore.galaxycorecore.configuration.PlayerLoader;
import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.debug.OneHitDebug;
import org.bukkit.entity.Player;

@Getter
public class CoinsBinding {
    private final Player player;
    private final CoinDAO dao;

    public CoinsBinding(Player player) {
        this.player = player;
        this.dao = new CoinDAO(PlayerLoader.load(player), OneHit.getInstance());
    }

    public long getCoins() {
        return dao.get();
    }

    public void increase(long coins) {
        OneHitDebug.debug(player + ": Add " + coins + " Coins");
        dao.transact(null, -coins, "OHGetCoinsForKill");
    }

    public void decrease(long coins, String reason) {
        OneHitDebug.debug(player + ": Remove " + coins + " Coins");
        try {
            dao.transact(null, coins, "OHRemoveCoins::" + reason);
        }catch (PlayerTransactionError ignored) {
            dao.transact(null, dao.get(), "OHRemoveCoins::" + reason);
        }

    }

    public boolean hasCoins(long coins) {
        return getCoins() >= coins;
    }
}
