package sscefalix.the.best.sxpillars.managers.arena;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import sscefalix.the.best.sxpillars.SXPillars;

public class ArenaManager {
    public void clearArena(Arena arena) {
        World world = Bukkit.getWorld(arena.world());

        if (world == null) return;

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = -64; x <= 64; x++) {
                    for (int z = -64; z <= 64; z++) {
                        for (int y = 0; y <= 255; y++) {
                            Location loc = new Location(world, x, y, z);

                            Block block = world.getBlockAt(loc);

                            if (block.getType() == Material.BEDROCK) continue;

                            world.getBlockAt(loc).setType(Material.AIR);
                        }
                    }
                }
            }
        };

        runnable.runTask(SXPillars.getInstance());
    }


}
