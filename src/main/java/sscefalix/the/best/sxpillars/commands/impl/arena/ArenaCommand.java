package sscefalix.the.best.sxpillars.commands.impl.arena;

import org.bukkit.command.CommandSender;
import sscefalix.the.best.sxpillars.commands.DefaultCommand;

import java.util.List;

public class ArenaCommand extends DefaultCommand {
    public ArenaCommand() {
        super("arena", "Команда для управления аренами.", List.of(
                "sxpillars.commands.arena"
        ), List.of(
                "&7&m&l--------&r&e Команды администратора &7&m&l--------",
                "&0",
                "&e● &7Создать арену — &e/arena create",
                "&e● &7Установить точку появления — &e/arena setlocation",
                "&e● &7Включить/выключить арену — &e/arena setenabled",
                "&0",
                "&7&m&l------------&r&e Команды игрока &7&m&l------------",
                "&0",
                "&e● &7Присоединиться к арене — &e/arena join",
                "&e● &7Выйти с арены — &e/arena quit",
                "&0",
                "&7&m&l------------&r&e By &b&lsscefalix &c❤ &7&m&l------------"
        ));

        addSubCommand(new ArenaCreateCommand());
//        addSubCommand(new ArenaRenameCommand());
        addSubCommand(new ArenaSetLocationCommand());
        addSubCommand(new ArenaSetEnabledCommand());
        addSubCommand(new ArenaJoinCommand());
        addSubCommand(new ArenaQuitCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
    }
}
