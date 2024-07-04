package sscefalix.the.best.sxpillars.commands.impl.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.arena.Arena;

import java.util.Arrays;
import java.util.List;

public class ArenaCreateCommand extends DefaultSubCommand {
    public ArenaCreateCommand() {
        super("create", "Создание арены.", List.of(
                "sxpillars.commands.arena.create"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorManager.colorize("&cКоманду может выполнить только игрок."));
            return;
        }

        if (args.length < 2) { // /arena create <name> <minPlayers> <maxPlayers>
            sender.sendMessage(ColorManager.colorize("&7Используйте: &e/arena create <название> <минимум игроков> <максимум игроков>"));
            return;
        }

        List<String> arguments = Arrays.stream(args).toList().subList(0, 3);

        String name = arguments.get(0);
        String min = arguments.get(1);
        String max = arguments.get(2);

        int minPlayers, maxPlayers;

        if (SXPillars.getInstance().getArenaStorage().getArena(name) != null) {
            sender.sendMessage(ColorManager.colorize("&cАрена " + name + " уже существует."));
            return;
        }

        try {
            minPlayers = Integer.parseInt(min);
            maxPlayers = Integer.parseInt(max);
        } catch (NumberFormatException e) {
            sender.sendMessage(ColorManager.colorize("&cАргументы <минимум игроков>,<максимум игроков> должны быть числом."));
            return;
        }

        if (minPlayers < 2) {
            sender.sendMessage(ColorManager.colorize("&cМинимальное количество игроков 2."));
            return;
        }

        if (maxPlayers > 16) {
            sender.sendMessage(ColorManager.colorize("&cМаксимальное количество игроков 16."));
            return;
        }

        if (minPlayers > maxPlayers) {
            sender.sendMessage(ColorManager.colorize("&cМинимальное количество игроков должно быть меньше или равно максимальному."));
            return;
        }

        Arena arena = new Arena(name, minPlayers, maxPlayers, player.getWorld().getName(), false, List.of());

        SXPillars.getInstance().getArenaStorage().createArena(arena);

        sender.sendMessage(ColorManager.colorize("&aАрена " + name + " успешно создана."));
    }
}
