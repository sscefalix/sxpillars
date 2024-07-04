package sscefalix.the.best.sxpillars.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.api.events.PlayerDeathGameEvent;
import sscefalix.the.best.sxpillars.api.events.PlayerLobbyTeleportEvent;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.game.Game;
import sscefalix.the.best.sxpillars.managers.game.GameManager;

public class PlayerHandler implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GameManager gameManager = SXPillars.getInstance().getGameManager();

        gameManager.quitGame(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        GameManager gameManager = SXPillars.getInstance().getGameManager();

        Game game = gameManager.getPlayerGame(player);

        if (game == null) return;

        if (killer != null) {
            game.addPlayerInGameKill(killer);
        }

        game.addPlayerInGameDeath(player);

//        event.setCancelled(true);
        event.setDroppedExp(0);
        event.deathMessage(null);
        event.getDrops().clear();

        PlayerDeathGameEvent playerDeathGameEvent = new PlayerDeathGameEvent(game, player);

        SXPillars.getInstance().getServer().getPluginManager().callEvent(playerDeathGameEvent);
    }

    @EventHandler
    public void onPlayerLobbyTeleport(PlayerLobbyTeleportEvent event) {
        event.getPlayer().sendMessage(ColorManager.colorize("&aВы перемещены в лобби."));
    }
}
