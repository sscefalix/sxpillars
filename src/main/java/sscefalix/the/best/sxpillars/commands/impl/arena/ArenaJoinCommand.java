package sscefalix.the.best.sxpillars.commands.impl.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.game.Game;
import sscefalix.the.best.sxpillars.managers.game.GameManager;
import sscefalix.the.best.sxpillars.managers.storage.interfaces.SettingsStorage;

import java.util.List;

public class ArenaJoinCommand extends DefaultSubCommand {
    public ArenaJoinCommand() {
        super("join", "Присоединиться к арене.", List.of(
                "sxpillars.commands.arena.join"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorManager.colorize("&cКоманду может выполнить только игрок."));
            return;
        }

        GameManager gameManager = SXPillars.getInstance().getGameManager();

        if (gameManager.getPlayerGame(player) != null) {
            sender.sendMessage(ColorManager.colorize("&cВы уже на арене. Выйти - &e/arena quit&c."));
            return;
        }

        Game game = gameManager.findGame();

        if (game == null) {
            sender.sendMessage(ColorManager.colorize("&cВ данный момент нет свободных арен."));
            return;
        }

        SettingsStorage settingsStorage = SXPillars.getInstance().getSettingsStorage();

        if (settingsStorage.getLobby() == null) {
            sender.sendMessage(ColorManager.colorize("&cТочка лобби не установлена. Невозможно запустить игру."));
            return;
        }

        gameManager.joinGame(game, player);
    }
}
