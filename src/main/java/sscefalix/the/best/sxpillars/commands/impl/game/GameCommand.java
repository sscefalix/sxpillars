package sscefalix.the.best.sxpillars.commands.impl.game;

import org.bukkit.command.CommandSender;
import sscefalix.the.best.sxpillars.commands.DefaultCommand;

import java.util.List;

public class GameCommand  extends DefaultCommand {
    public GameCommand() {
        super("game", "Команда для управления мини игрой.", List.of(
                "sxpillars.commands.game"
        ), List.of(
                "&7&m&l--------&r&e Команды администратора &7&m&l--------",
                "&0",
                "&e● &7Установить точку лобби — &e/game setlobby",
//                "&0",
//                "&7&m&l------------&r&e Команды игрока &7&m&l------------",
//                "&0",
//                "&e● &7Присоединиться к арене — &e/arena join",
//                "&e● &7Выйти с арены — &e/arena quit",
                "&0",
                "&7&m&l------------&r&e By &b&lsscefalix &c❤ &7&m&l------------"
        ));

        addSubCommand(new GameSetLobbyCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
    }
}
