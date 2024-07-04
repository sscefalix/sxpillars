package sscefalix.the.best.sxpillars.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import sscefalix.the.best.sxpillars.managers.game.Game;

public class PlayerDeathGameEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Game game;
    private final Player player;

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public PlayerDeathGameEvent(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}
