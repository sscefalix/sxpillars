package sscefalix.the.best.sxpillars.managers;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import sscefalix.the.best.sxpillars.commands.DefaultCommand;
import sscefalix.the.best.sxpillars.commands.impl.arena.ArenaCommand;
import sscefalix.the.best.sxpillars.commands.impl.game.GameCommand;
import sscefalix.the.best.sxpillars.commands.impl.pillars.PillarsCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final JavaPlugin plugin;

    private final List<DefaultCommand> commands = new ArrayList<>();

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;

        commands.add(new ArenaCommand());
        commands.add(new GameCommand());
        commands.add(new PillarsCommand());
    }

    public void loadCommands() {
        for (DefaultCommand command : commands) {
            PluginCommand pluginCommand = plugin.getCommand(command.getCommandName());

            if (pluginCommand == null) {
                plugin.getLogger().warning("Не удалось найти команду `" + command.getCommandName() + "` команда не была загружена.");
                continue;
            }

            pluginCommand.setExecutor(command);
            plugin.getLogger().info("Команда `" + command.getCommandName() + "` была успешно загружена.");
        }
    }

    public void unloadCommands() {
    }
}
