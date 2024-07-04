package sscefalix.the.best.sxpillars.managers.storage.interfaces;

import sscefalix.the.best.sxpillars.managers.arena.Arena;

import java.util.List;

public interface ArenaStorage {
    void createArena(Arena arena);

    List<Arena> getArenaList();

    Arena getArena(String name);

    void enableArena(Arena arena);

    void disableArena(Arena arena);

    void updateArena(String name, Arena arena);

    void deleteArena(Arena arena);
}
