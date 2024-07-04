package sscefalix.the.best.sxpillars.handlers;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkHandler implements Listener {
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) return;

        Entity[] entities = event.getChunk().getEntities();

        boolean onlySpectators = true;

        for (Entity entity : entities) {
            if (!(entity instanceof Player player)) continue;

            if (player.getGameMode() != GameMode.SPECTATOR) {
                onlySpectators = false;
                break;
            }
        }

        if (onlySpectators) {
            event.getChunk().unload(true);
        }
    }
}
