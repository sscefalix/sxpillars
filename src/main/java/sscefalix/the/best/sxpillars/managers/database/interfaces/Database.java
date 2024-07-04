package sscefalix.the.best.sxpillars.managers.database.interfaces;

import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.managers.database.records.PlayerStats;

public interface Database {
    String playerStatsTable = "player_stats";

    void init();

    PlayerStats getPlayerStats(Player player);
    void updatePlayerStats(Player player, PlayerStats stats);
}
