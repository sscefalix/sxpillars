package sscefalix.the.best.sxpillars.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import sscefalix.the.best.sxpillars.managers.game.Game;

public class GameEndEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Game game;
    private final Player winner;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public GameEndEvent(Game game, Player winner) {
        this.game = game;
        this.winner = winner;
    }

    public Game getGame() {
        return game;
    }

    public Player getWinner() {
        return winner;
    }
}
