package sscefalix.the.best.sxpillars;

import org.bukkit.plugin.java.JavaPlugin;
import sscefalix.the.best.sxpillars.handlers.ChunkHandler;
import sscefalix.the.best.sxpillars.handlers.GameHandler;
import sscefalix.the.best.sxpillars.handlers.PlayerHandler;
import sscefalix.the.best.sxpillars.managers.CommandManager;
import sscefalix.the.best.sxpillars.managers.arena.ArenaManager;
import sscefalix.the.best.sxpillars.managers.storage.interfaces.ArenaStorage;
import sscefalix.the.best.sxpillars.managers.storage.interfaces.SettingsStorage;
import sscefalix.the.best.sxpillars.managers.storage.impl.YamlArenaStorage;
import sscefalix.the.best.sxpillars.managers.storage.impl.YamlSettingsStorage;
import sscefalix.the.best.sxpillars.managers.database.interfaces.Database;
import sscefalix.the.best.sxpillars.managers.database.impl.SQLiteDatabase;
import sscefalix.the.best.sxpillars.managers.game.GameManager;

import java.io.File;

public final class SXPillars extends JavaPlugin {
    private static SXPillars instance;
    private Database database;

    // Manager
    private ArenaManager arenaManager;
    private CommandManager commandManager;
    private GameManager gameManager;

    // Storage
    private SettingsStorage settingsStorage;
    private ArenaStorage arenaStorage;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        commandManager = new CommandManager(this);
        gameManager = new GameManager();
        arenaManager = new ArenaManager();

        commandManager.loadCommands();

        getServer().getPluginManager().registerEvents(new GameHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
        getServer().getPluginManager().registerEvents(new ChunkHandler(), this);

        if (getDataFolder().mkdir()) {
            getLogger().info("Директория конфигураций успешно создана.");
        }

        settingsStorage = new YamlSettingsStorage(new File(getDataFolder(), "settings.yml"));
        arenaStorage = new YamlArenaStorage(new File(getDataFolder(), "arenas.yml"));

        try {
            database = new SQLiteDatabase(new File(getDataFolder(), "stats.sqlite"));

            database.init();
        } catch (Exception e) {
            getLogger().severe(e.getMessage());
        }

        int loadedGames = gameManager.loadGames();
        getLogger().info(loadedGames + " арен было загружено.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        commandManager.unloadCommands();
    }

    public static SXPillars getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    // Manager
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    // Storage
    public SettingsStorage getSettingsStorage() {
        return settingsStorage;
    }

    public ArenaStorage getArenaStorage() {
        return arenaStorage;
    }
}
