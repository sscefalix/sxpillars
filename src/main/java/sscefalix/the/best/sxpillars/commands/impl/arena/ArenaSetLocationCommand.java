package sscefalix.the.best.sxpillars.commands.impl.arena;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.arena.Arena;

import java.util.ArrayList;
import java.util.List;

public class ArenaSetLocationCommand extends DefaultSubCommand {
    public ArenaSetLocationCommand() {
        super("setlocation", "Установить точку появления игрока.", List.of(
                "sxpillars.commands.arena.setlocation"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorManager.colorize("&cКоманду может выполнить только игрок."));
            return;
        }

        if (args.length < 2) { // /arena setlocation <name> <index>
            sender.sendMessage(ColorManager.colorize("&7Используйте: &e/arena setlocation <название> <индекс игрока>"));
            return;
        }

        String name = args[0];
        String i = args[1];

        int index;

        Arena arena = SXPillars.getInstance().getArenaStorage().getArena(name);

        if (arena == null) {
            sender.sendMessage(ColorManager.colorize("&cАрена " + name + " не найдена."));
            return;
        }

        try {
            index = Integer.parseInt(i);
        } catch (NumberFormatException e) {
            sender.sendMessage(ColorManager.colorize("&cАргумент <индекс игрока> должен быть числом."));
            return;
        }

        if (index > arena.maxPlayers()) {
            sender.sendMessage(ColorManager.colorize("&cМаксимальный индекс точки появления равен " + arena.maxPlayers()));
            return;
        }

        List<List<Integer>> locations = new ArrayList<>();

        Location location = player.getLocation();

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        for (int j = 0; j < arena.maxPlayers(); j++) {
            if (j + 1 == index) {
                locations.add(List.of(x, y, z));
            } else {
                locations.add(arena.locations().get(j));
            }
        }

        SXPillars.getInstance().getArenaStorage().updateArena(
                arena.name(),
                new Arena(
                        arena.name(),
                        arena.minPlayers(),
                        arena.maxPlayers(),
                        arena.world(),
                        arena.enabled(),
                        locations
                )
        );

        sender.sendMessage(ColorManager.colorize("&aТочка появления игрока " + index + " установлена: " + String.join(" ", List.of(String.valueOf(x), String.valueOf(y), String.valueOf(z)))));
    }
}
