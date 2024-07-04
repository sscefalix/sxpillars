package sscefalix.the.best.sxpillars.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import sscefalix.the.best.sxpillars.managers.game.Game;

public class GameStartEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Game game;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public GameStartEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
