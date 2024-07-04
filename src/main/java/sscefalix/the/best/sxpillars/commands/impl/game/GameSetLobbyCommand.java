package sscefalix.the.best.sxpillars.commands.impl.game;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;

import java.util.List;

public class GameSetLobbyCommand extends DefaultSubCommand {
    public GameSetLobbyCommand() {
        super("setlobby", "Установить точку лобби.", List.of(
                "sxpillars.command.game.setlobby"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorManager.colorize("&cКоманду может выполнить только игрок."));
            return;
        }

        Location loc = player.getLocation();

        SXPillars.getInstance().getSettingsStorage().setLobby(loc);

        player.sendMessage(ColorManager.colorize("&aТочка лобби успешно установлена."));
    }
}
