package sscefalix.the.best.sxpillars.managers.database.impl;

import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.managers.database.interfaces.Database;
import sscefalix.the.best.sxpillars.managers.database.records.PlayerStats;

import java.io.File;
import java.sql.*;

public class SQLiteDatabase implements Database {
    private final String url;

    public SQLiteDatabase(File file) throws SQLException {
        try {
            url = "jdbc:sqlite:" + file.getAbsolutePath();
            Class.forName("org.sqlite.JDBC").getConstructor().newInstance();
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }

        try (Connection connection = DriverManager.getConnection(url); Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(
                    "CREATE TABLE IF NOT EXISTS `%s` (" +
                            "`nickname` TEXT NOT NULL UNIQUE, " +
                            "`kills` INTEGER NOT NULL DEFAULT 0, " +
                            "`deaths` INTEGER NOT NULL DEFAULT 0, " +
                            "`wins` INTEGER NOT NULL DEFAULT 0, " +
                            "`losses` INTEGER NOT NULL DEFAULT 0)",
                    playerStatsTable
            ));
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Override
    public void init() {

    }

    @Override
    public PlayerStats getPlayerStats(Player player) {
        String query = String.format("SELECT * FROM `%s` WHERE nickname = ?", playerStatsTable);

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getName());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new PlayerStats(resultSet.getString(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new PlayerStats(player.getName(), 0, 0, 0, 0);
    }

    @Override
    public void updatePlayerStats(Player player, PlayerStats stats) {
        String query = String.format("INSERT OR REPLACE INTO `%s` (`nickname`, `kills`, `deaths`, `wins`, `losses`) VALUES (?, ?, ?, ?, ?);", playerStatsTable);

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getName().toLowerCase());
            statement.setInt(2, stats.kills());
            statement.setInt(3, stats.deaths());
            statement.setInt(4, stats.wins());
            statement.setInt(5, stats.losses());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
