package sscefalix.the.best.sxpillars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.PermissionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DefaultCommand implements CommandExecutor, TabCompleter {
    private final String commandName;
    private final String commandDescription;
    private final List<String> commandPermissions;

    private final List<DefaultSubCommand> subCommands = new ArrayList<>();

    private final List<String> usageMessage;

    public DefaultCommand(String commandName, String commandDescription, List<String> commandPermissions, List<String> usageMessage) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;

        this.commandPermissions = commandPermissions;

        this.usageMessage = usageMessage;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public List<String> getCommandPermissions() {
        return commandPermissions;
    }

    public void addSubCommand(DefaultSubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public List<DefaultSubCommand> getSubCommands() {
        return subCommands;
    }

    public List<String> getUsageMessage() {
        return usageMessage;
    }

    public List<String> getPermissionMessage(String permission) {
        return List.of("&cУ вас недостаточно прав. &7(" + permission + ")");
    }

    public abstract void execute(CommandSender sender, String[] arguments);

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(PermissionManager.hasPermission(commandSender, getCommandPermissions()))) {
            for (String permissionMessage : getPermissionMessage(getCommandPermissions().get(0))) {
                commandSender.sendMessage(ColorManager.colorize(permissionMessage));
            }
            return true;
        }

        String subCommandName = args.length > 0 ? args[0] : "";
        String[] subCommandArgs = Arrays.copyOfRange(args, args.length > 0 ? 1 : 0, args.length);

        if (!getSubCommands().isEmpty() && args.length > 0) {
            boolean searched = false;

            for (DefaultSubCommand subCommand : getSubCommands()) {
                if (subCommand.getCommandName().equalsIgnoreCase(subCommandName)) {
                    if (subCommand.checkPermission(commandSender)) {
                        subCommand.execute(commandSender, subCommandArgs);
                    }

                    searched = true;
                }
            }

            if (!searched) {
                for (String usageMessage : getUsageMessage()) {
                    commandSender.sendMessage(ColorManager.colorize(usageMessage));
                }
            }
        } else {
            execute(commandSender, args);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!getSubCommands().isEmpty()) {
            if (args.length == 1) {
//                List<String> tabCompletions = new ArrayList<>();
//
//                String subCommandName = args[0];
//
//                for (DefaultSubCommand subCommand : getSubCommands()) {
//                    if (subCommand.getCommandName().equalsIgnoreCase(subCommandName)) {
//                        if (subCommand.checkPermission(commandSender)) {
//                            tabCompletions.add(subCommand.getCommandName());
//                        }
//                    }
//                }
//
//                return tabCompletions;

                return getSubCommands().stream().map(DefaultSubCommand::getCommandName).toList();
            }
//            } else if (args.length > 1) {
//                List<String> tabCompletions = new ArrayList<>();
//
//                String subCommandName = args[0];
//
//                for (DefaultSubCommand subCommand : getSubCommands()) {
//                    if (subCommand.getCommandName().equalsIgnoreCase(subCommandName)) {
//                        if (subCommand.checkPermission(commandSender)) {
//                            tabCompletions.add(subCommand.getCommandName());
//                        }
//                    }
//                }
//
//                return tabCompletions;
//            }
        }

        return new ArrayList<>();
    }
}
