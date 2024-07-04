package sscefalix.the.best.sxpillars.managers.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.api.events.GameStartEvent;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.arena.Arena;

import java.util.HashMap;
import java.util.List;

public class Game {
    private static boolean timerIsRunning = false;

    private final Arena arena;
    private GameState state;
    private final List<Player> players;
    private final HashMap<Player, HashMap<String, Integer>> inGameStats;

    public Game(Arena arena, GameState state, List<Player> players) {
        this.arena = arena;
        this.state = state;
        this.players = players;
        this.inGameStats = new HashMap<>();
    }

    public Arena getArena() {
        return arena;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public HashMap<Player, HashMap<String, Integer>> getInGameStats() {
        return inGameStats;
    }

    public int getPlayerInGameKills(Player player) {
        return inGameStats.get(player).get("kills");
    }

    public void addPlayerInGameKill(Player player) {
        inGameStats.get(player).put("kills", getPlayerInGameKills(player) + 1);
    }

    public int getPlayerInGameDeaths(Player player) {
        return inGameStats.get(player).get("deaths");
    }

    public void addPlayerInGameDeath(Player player) {
        inGameStats.get(player).put("deaths", getPlayerInGameDeaths(player) + 1);
    }

    public void startTimer() {
        Game game = this;

        GameManager gameManager = SXPillars.getInstance().getGameManager();

        BukkitRunnable runnable = new BukkitRunnable() {
            private int timeToStart = 30;

            @Override
            public void run() {
                if (players.size() == arena.maxPlayers()) {
                    timeToStart = Math.min(timeToStart, 5);
                }

                setState(GameState.STARTING);

                if (players.size() < arena.minPlayers()) {
                    timeToStart = 30;

                    cancel();

                    timerIsRunning = false;

                    setState(GameState.WAITING);

                    return;
                }

                if (timeToStart <= 0) {
                    timeToStart = 0;

                    cancel();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            gameManager.startGame(game);
                        }
                    }.runTask(SXPillars.getInstance());

                    return;
                }

                for (Player p : players) {
                    p.sendMessage(ColorManager.colorize("&fДо начала игры осталось: &e" + timeToStart + " &fсекунд."));
                }

                timeToStart--;
            }
        };

        if (timerIsRunning) return;

        timerIsRunning = true;

        runnable.runTaskTimerAsynchronously(SXPillars.getInstance(), 0, 20);
    }

    public void start() {
        World world = Bukkit.getWorld(getArena().world());

        Game currentGame = SXPillars.getInstance().getGameManager().getPlayerGame(getPlayers().get(0));

        if (world == null) return;

        WorldBorder border = world.getWorldBorder();

        BukkitRunnable runnableItems = new BukkitRunnable() {
            @Override
            public void run() {
                if (currentGame.getState() == GameState.PLAYING) {
                    for (Player p : getPlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR).toList()) {
                        p.getInventory().addItem(GameItem.getRandomItem());
                    }
                } else {
                    cancel();
                }
            }
        };

        BukkitRunnable runnableBorder = new BukkitRunnable() {
            @Override
            public void run() {
                if (currentGame.getState() == GameState.PLAYING) {
                    border.setSize(border.getSize() > 0 ? border.getSize() - 1 : 0);
                } else {
                    cancel();
                }
            }
        };

        if (currentGame.getState() == GameState.PLAYING) {
            GameStartEvent gameStartEvent = new GameStartEvent(this);

            SXPillars.getInstance().getServer().getPluginManager().callEvent(gameStartEvent);

            for (Player p : getPlayers()) {
                Location loc = p.getLocation();

                p.getInventory().clear();

                HashMap<String, Integer> stats = new HashMap<>();

                stats.put("kills", 0);
                stats.put("deaths", 0);

                inGameStats.put(p, stats);

                for (int y = 0; y < 3; y++) {
                    new Location(world, loc.getBlockX() + 1, loc.getBlockY() + y, loc.getBlockZ()).getBlock().setType(Material.AIR);
                    new Location(world, loc.getBlockX() - 1, loc.getBlockY() + y, loc.getBlockZ()).getBlock().setType(Material.AIR);
                    new Location(world, loc.getBlockX(), loc.getBlockY() + y, loc.getBlockZ() + 1).getBlock().setType(Material.AIR);
                    new Location(world, loc.getBlockX(), loc.getBlockY() + y, loc.getBlockZ() - 1).getBlock().setType(Material.AIR);
                }
            }

            border.setCenter(0, 0);
            border.setSize(128);

            border.setWarningTime(10);
            border.setWarningDistance(10);

            runnableItems.runTaskTimerAsynchronously(SXPillars.getInstance(), 0, 20 * 5);
            runnableBorder.runTaskTimer(SXPillars.getInstance(), 0, 20 * 5);
        }
    }

    public void end() {
        GameManager gameManager = SXPillars.getInstance().getGameManager();

        for (Player p : getPlayers()) {
            p.getInventory().clear();
        }

        gameManager.endGame(this);
    }
}
