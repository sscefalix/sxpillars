package sscefalix.the.best.sxpillars.managers.storage.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.managers.storage.interfaces.SettingsStorage;

import java.io.File;
import java.io.IOException;

public class YamlSettingsStorage implements SettingsStorage {
    private final File file;
    private YamlConfiguration configuration;

    public YamlSettingsStorage(File file) {
        this.file = file;

        if (!file.exists()) {
            try {
                boolean ignored = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void setLobby(Location lobby) {
        String path = "game.lobby.";

        configuration.set(path + "world", lobby.getWorld().getName());
        configuration.set(path + "x", lobby.getBlockX());
        configuration.set(path + "y", lobby.getBlockY());
        configuration.set(path + "z", lobby.getBlockZ());

        save();
    }

    @Override
    public Location getLobby() {
        ConfigurationSection section = configuration.getConfigurationSection("game.lobby");

        if (section == null) return null;

        String world = section.getString("world");
        int x = section.getInt("x");
        int y = section.getInt("y");
        int z = section.getInt("z");

        if (world == null) return null;

        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    private void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            SXPillars.getInstance().getLogger().warning(e.getMessage());
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }
}
