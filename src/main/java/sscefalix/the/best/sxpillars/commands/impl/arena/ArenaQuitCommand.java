package sscefalix.the.best.sxpillars.commands.impl.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.game.GameManager;

import java.util.List;

public class ArenaQuitCommand extends DefaultSubCommand {
    public ArenaQuitCommand() {
        super("quit", "Присоединиться к арене.", List.of(
                "sxpillars.commands.arena.quit"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorManager.colorize("&cКоманду может выполнить только игрок."));
            return;
        }

        GameManager gameManager = SXPillars.getInstance().getGameManager();

        if (gameManager.getPlayerGame(player) == null) {
            sender.sendMessage(ColorManager.colorize("&cВы не находитесь на арене. Войти - &e/arena join&c."));
            return;
        }

        gameManager.quitGame(player);
    }
}
