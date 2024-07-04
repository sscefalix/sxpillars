package sscefalix.the.best.sxpillars.handlers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import sscefalix.the.best.sxpillars.api.events.*;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.arena.Arena;
import sscefalix.the.best.sxpillars.managers.game.Game;
import sscefalix.the.best.sxpillars.managers.game.GameState;

import java.util.List;

public class GameHandler implements Listener {
    @EventHandler
    public void onPlayerJoinGame(PlayerJoinGameEvent event) {
        Game game = event.getGame();
        Arena arena = game.getArena();
        Player player = event.getPlayer();

        for (Player p : game.getPlayers()) {
            p.sendMessage(ColorManager.colorize("&7Игрок &e" + player.getName() + " &7присоединился к игре. &f[&a" + game.getPlayers().size() + "&f/&a" + arena.maxPlayers() + "&f]"));
        }
    }

    @EventHandler
    public void onPlayerQuitGame(PlayerQuitGameEvent event) {
        Game game = event.getGame();
        Arena arena = game.getArena();
        Player player = event.getPlayer();

        for (Player p : game.getPlayers()) {
            p.sendMessage(ColorManager.colorize("&7Игрок &e" + player.getName() + " &7отключился от игры. &f[&a" + (game.getPlayers().size() - 1) + "&f/&a" + arena.maxPlayers() + "&f]"));
        }

        if (game.getState() == GameState.PLAYING) {
            checkAlivePlayers(game);
        }
    }

    @EventHandler
    public void onPreStartGame(PreStartGameEvent event) {
        Game game = event.getGame();

        if (game.getPlayers().size() >= game.getArena().minPlayers()) {
            game.startTimer();
        }
    }

    @EventHandler
    public void onPlayerDeathGame(PlayerDeathGameEvent event) {
        Player player = event.getPlayer();
        Player killer = player.getKiller();
        Game game = event.getGame();

        player.setHealth(20.0);
        player.setGameMode(GameMode.SPECTATOR);

        for (Player p : game.getPlayers()) {
            if (player.isDead()) {
                player.spigot().respawn();
            }
            String message = killer == null ? "&7Игрок &e" + player.getName() + " &7умер." : "&7Игрок &e" + player.getName() + " &7был убит игроком &e" + killer.getName() + "&7.";
            p.sendMessage(ColorManager.colorize(message));
        }

        if (game.getState() == GameState.PLAYING) {
            checkAlivePlayers(game);
        }
    }

    private void checkAlivePlayers(Game game) {
        List<Player> alivePlayers = game.getPlayers().stream().filter(p -> p.getGameMode() != GameMode.SPECTATOR).toList();

        if (alivePlayers.size() == 1) {
            for (Player p : game.getPlayers()) {
                p.sendMessage(ColorManager.colorize("&aИгра завершена. Победитель: &e" + alivePlayers.get(0).getName() + "&a."));
            }

            game.end();
        }
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Game game = event.getGame();

        for (Player p : game.getPlayers()) {
            p.sendMessage(ColorManager.colorize("&aИгра началась!"));
        }
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        Game game = event.getGame();

        Player winner = event.getWinner();

        for (Player p : game.getPlayers()) {
            p.sendMessage(ColorManager.colorize("&aИгра завершена! Победитель: &e" + winner.getName() + "&a."));
        }
    }
}
