package sscefalix.the.best.sxpillars.managers.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.api.events.PlayerJoinGameEvent;
import sscefalix.the.best.sxpillars.api.events.PlayerLobbyTeleportEvent;
import sscefalix.the.best.sxpillars.api.events.PlayerQuitGameEvent;
import sscefalix.the.best.sxpillars.api.events.PreStartGameEvent;
import sscefalix.the.best.sxpillars.managers.arena.Arena;
import sscefalix.the.best.sxpillars.managers.database.interfaces.Database;
import sscefalix.the.best.sxpillars.managers.database.records.PlayerStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager {
    private final List<Game> games;
    private final HashMap<Player, Game> playerGame;

    public GameManager() {
        this.games = new ArrayList<>();
        this.playerGame = new HashMap<>();
    }

    public List<Game> getGames() {
        return games;
    }

    public Game getPlayerGame(Player player) {
        return playerGame.get(player);
    }

    public void setPlayerGame(Player player, Game game) {
        playerGame.remove(player);
        playerGame.put(player, game);
    }

    public int loadGames() {
        int count = 0;

        for (Arena arena : SXPillars.getInstance().getArenaStorage().getArenaList()) {
            if (arena.enabled()) {
                Game game = new Game(arena, GameState.WAITING, new ArrayList<>());

                games.add(game);

                count++;
            }
        }

        return count;
    }

    public int reloadGames() {
        for (Game game : games) {
            game.end();
        }

        games.clear();

        return loadGames();
    }

    public Game findGame() {
        Game find = null;

        for (Game game : games) {
            System.out.println(game);

            if (List.of(GameState.WAITING, GameState.PLAYING).contains(game.getState()) && game.getArena().enabled()) {
                if (game.getArena().maxPlayers() > game.getPlayers().size()) {
                    find = game;
                }
            }
        }

        return find;
    }

    public void teleportToLobby(Player player) {
        Location loc = SXPillars.getInstance().getSettingsStorage().getLobby();

        if (loc != null) {
            player.teleport(loc);

            PlayerLobbyTeleportEvent playerLobbyTeleportEvent = new PlayerLobbyTeleportEvent(player);

            SXPillars.getInstance().getServer().getPluginManager().callEvent(playerLobbyTeleportEvent);
        }
    }

    public void joinGame(Game game, Player player) {
        if (game.getPlayers().size() < game.getArena().maxPlayers()) {
            Arena arena = game.getArena();

            World world = player.getServer().getWorld(arena.world());

            if (world == null) return;

            game.getPlayers().add(player);
            setPlayerGame(player, game);

            PlayerJoinGameEvent playerJoinGameEvent = new PlayerJoinGameEvent(player, game);
            PreStartGameEvent preStartGameEvent = new PreStartGameEvent(game);

            player.getServer().getPluginManager().callEvent(playerJoinGameEvent);
            player.getServer().getPluginManager().callEvent(preStartGameEvent);

            player.getInventory().clear();

            for (List<Integer> location : arena.locations()) {
                Location loc = new Location(world, location.get(0) + 0.5, location.get(1), location.get(2) + 0.5);

                if (loc.getNearbyPlayers(1).isEmpty()) {
                    for (int y = 0; y < 3; y++) {
                        new Location(world, loc.getBlockX() + 1, loc.getBlockY() + y, loc.getBlockZ()).getBlock().setType(Material.BARRIER);
                        new Location(world, loc.getBlockX() - 1, loc.getBlockY() + y, loc.getBlockZ()).getBlock().setType(Material.BARRIER);
                        new Location(world, loc.getBlockX(), loc.getBlockY() + y, loc.getBlockZ() + 1).getBlock().setType(Material.BARRIER);
                        new Location(world, loc.getBlockX(), loc.getBlockY() + y, loc.getBlockZ() - 1).getBlock().setType(Material.BARRIER);
                    }

                    player.teleport(loc);

                    break;
                }
            }
        }
    }

    public void quitGame(Player player) {
        Game game = getPlayerGame(player);

        if (game == null) return;

        PlayerQuitGameEvent playerQuitGameEvent = new PlayerQuitGameEvent(player, game);

        player.getServer().getPluginManager().callEvent(playerQuitGameEvent);

        player.getInventory().clear();

        Location loc = player.getLocation();
        World world = loc.getWorld();

        for (int y = 0; y < 3; y++) {
            new Location(world, loc.getBlockX() + 1, loc.getBlockY() + y, loc.getBlockZ()).getBlock().setType(Material.AIR);
            new Location(world, loc.getBlockX() - 1, loc.getBlockY() + y, loc.getBlockZ()).getBlock().setType(Material.AIR);
            new Location(world, loc.getBlockX(), loc.getBlockY() + y, loc.getBlockZ() + 1).getBlock().setType(Material.AIR);
            new Location(world, loc.getBlockX(), loc.getBlockY() + y, loc.getBlockZ() - 1).getBlock().setType(Material.AIR);
        }

        teleportToLobby(player);
        game.getPlayers().remove(player);
        setPlayerGame(player, null);
    }

    public void startGame(Game game) {
        game.setState(GameState.PLAYING);

        game.start();
    }

    public void endGame(Game game) {
        Player winner = game.getPlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toList().get(0);

        for (Player p : game.getPlayers()) {
            p.setGameMode(GameMode.SURVIVAL);

            teleportToLobby(p);

            setPlayerGame(p, null);

            SXPillars.getInstance().getArenaManager().clearArena(game.getArena());

            Database database = SXPillars.getInstance().getDatabase();

            HashMap<String, Integer> inGameStats = game.getInGameStats().get(p);
            PlayerStats currentStats = database.getPlayerStats(p);

            if (p.getName().equalsIgnoreCase(winner.getName())) {
                database.updatePlayerStats(p, new PlayerStats(p.getName(), inGameStats.get("kills"), inGameStats.get("deaths"), currentStats.wins() + 1, currentStats.losses()));
            } else {
                database.updatePlayerStats(p, new PlayerStats(p.getName(), inGameStats.get("kills"), inGameStats.get("deaths"), currentStats.wins(), currentStats.losses() + 1));
            }
        }

        game.setState(GameState.WAITING);
        game.getPlayers().clear();
    }

//    private List<BukkitRunnable> filterTask(Game game, String identifier) {
//        return game.runnables().stream().filter(bukkitRunnable -> {
//            try {
//                Field identifierField = bukkitRunnable.getClass().getDeclaredField("identifier");
//
//                identifierField.setAccessible(true);
//
//                String identifier_ = (String) identifierField.get();
//
//                if (Objects.equals(identifier_, identifier)) {
//                    return true;
//                }
//            } catch (NoSuchFieldException | IllegalAccessException ignored) {
//                return false;
//            }
//            return false;
//        }).toList();
//    }
}
