package sscefalix.the.best.sxpillars.managers.storage.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.managers.arena.Arena;
import sscefalix.the.best.sxpillars.managers.storage.interfaces.ArenaStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YamlArenaStorage implements ArenaStorage {
    private final File file;
    private YamlConfiguration configuration;

    public YamlArenaStorage(File file) {
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
    public void createArena(Arena arena) {
        String path = "arenas." + arena.name().toLowerCase() + ".";

        configuration.set(path + "min", arena.minPlayers());
        configuration.set(path + "max", arena.maxPlayers());
        configuration.set(path + "world", arena.world());
        configuration.set(path + "enabled", arena.enabled());

        for (int i = 0; i < arena.maxPlayers(); i++) {
            List<Integer> locations;

            try {
                locations = arena.locations().get(i);
            } catch (IndexOutOfBoundsException e) {
                locations = List.of();
            }

            configuration.set(path + "locations." + (i + 1), locations);
        }

        save();
    }

    @Override
    public List<Arena> getArenaList() {
        List<Arena> arenaList = new ArrayList<>();

        ConfigurationSection section = configuration.getConfigurationSection("arenas");

        if (section == null) {
            configuration.createSection("arenas");
            save();
        }

        for (String key : Objects.requireNonNull(configuration.getConfigurationSection("arenas")).getKeys(false)) {
            arenaList.add(getArena(key));
        }

        return arenaList;
    }

    @Override
    public Arena getArena(String name) {
        ConfigurationSection section = configuration.getConfigurationSection("arenas." + name.toLowerCase());

        if (section == null) return null;

        List<List<Integer>> locations = new ArrayList<>();

        for (int i = 0; i < section.getInt("max"); i++) {
            String path = "locations." + (i + 1);

            locations.add(section.getIntegerList(path));
        }

        return new Arena(name, section.getInt("min"), section.getInt("max"), section.getString("world"), section.getBoolean("enabled"), locations);
    }

    @Override
    public void enableArena(Arena arena) {
        Arena currentArena = getArena(arena.name());

        if (currentArena == null) return;

        updateArena(arena.name(), new Arena(
                arena.name(),
                arena.minPlayers(),
                arena.maxPlayers(),
                arena.world(),
                true,
                arena.locations()
        ));
    }

    @Override
    public void disableArena(Arena arena) {
        Arena currentArena = getArena(arena.name());

        if (currentArena == null) return;

        updateArena(arena.name(), new Arena(
                arena.name(),
                arena.minPlayers(),
                arena.maxPlayers(),
                arena.world(),
                false,
                arena.locations()
        ));
    }

    @Override
    public void updateArena(String name, Arena arena) {
        Arena oldArena = getArena(name);

        if (oldArena == null) return;

        deleteArena(oldArena);
        createArena(arena);
    }

    @Override
    public void deleteArena(Arena arena) {
        Arena currentArena = getArena(arena.name());

        if (currentArena == null) return;

        configuration.set("arenas." + currentArena.name().toLowerCase(), null);
        save();
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
