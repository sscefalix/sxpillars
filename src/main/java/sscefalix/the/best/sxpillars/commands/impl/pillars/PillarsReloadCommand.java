package sscefalix.the.best.sxpillars.commands.impl.pillars;

import org.bukkit.command.CommandSender;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.game.GameManager;

import java.util.List;

public class PillarsReloadCommand  extends DefaultSubCommand {
    public PillarsReloadCommand() {
        super("reload", "Перезагрузить плагин.", List.of(
                "sxpillars.commands.pillars.reload"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        GameManager gameManager = SXPillars.getInstance().getGameManager();

        int count = gameManager.reloadGames();

        sender.sendMessage(ColorManager.colorize("&aПлагин был перезагружен. Загружено &e" + count + " арен&a."));
    }
}
