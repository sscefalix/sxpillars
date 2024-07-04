package sscefalix.the.best.sxpillars.commands;

import org.bukkit.command.CommandSender;
import sscefalix.the.best.sxpillars.managers.ColorManager;

import java.util.List;

public abstract class DefaultSubCommand {
    private final String commandName;
    private final String commandDescription;
    private final List<String> commandPermissions;

    public DefaultSubCommand(String commandName, String commandDescription, List<String> commandPermissions) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;

        this.commandPermissions = commandPermissions;
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

    public List<String> getPermissionMessage(String permission) {
        return List.of("&cУ вас недостаточно прав. &7(" + permission + ")");
    }

    public boolean checkPermission(CommandSender sender) {
        if (!sender.hasPermission(commandPermissions.get(0))) {
            for (String permissionMessage : getPermissionMessage(commandPermissions.get(0))) {
                sender.sendMessage(ColorManager.colorize(permissionMessage));
            }
            return false;
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);
}
