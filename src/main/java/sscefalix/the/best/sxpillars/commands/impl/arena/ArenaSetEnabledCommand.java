package sscefalix.the.best.sxpillars.commands.impl.arena;

import org.bukkit.command.CommandSender;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.arena.Arena;

import java.util.List;

public class ArenaSetEnabledCommand extends DefaultSubCommand {
    public ArenaSetEnabledCommand() {
        super("setenabled", "Включает/выключает арену.", List.of(
                "sxpillars.commands.arena.setenabled"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2 || !List.of("true", "false").contains(args[1])) { // /arena setenabled <name> <true/false>
            sender.sendMessage(ColorManager.colorize("&7Используйте: &e/arena setenabled <название> <true/false>"));
            return;
        }

        String name = args[0];
        boolean enabled = Boolean.parseBoolean(args[1].toLowerCase());

        Arena arena = SXPillars.getInstance().getArenaStorage().getArena(name);

        if (arena == null) {
            sender.sendMessage(ColorManager.colorize("&cАрена " + name + " не найдена."));
            return;
        }

        boolean configurationRequired = false;

        for (List<Integer> location : arena.locations()) {
            if (location.size() != 3) {
                configurationRequired = true;
                break;
            }
        }

        if (configurationRequired) {
            sender.sendMessage(ColorManager.colorize("&cАрена " + name + " не настроена."));
            return;
        }

        if (enabled) SXPillars.getInstance().getArenaStorage().enableArena(arena);
        else SXPillars.getInstance().getArenaStorage().disableArena(arena);

        sender.sendMessage(ColorManager.colorize("&aАрена " + name + " была " + (enabled ? "&eвключена&a." : "&cвыключена&a.")));
        sender.sendMessage(ColorManager.colorize("&e&nДля обновления арен перезагрузите плагин&r &e— &b&n/pillars reload&e."));
    }
}
