package sscefalix.the.best.sxpillars.commands.impl.arena;

import org.bukkit.command.CommandSender;
import sscefalix.the.best.sxpillars.commands.DefaultSubCommand;

import java.util.List;

public class ArenaRenameCommand extends DefaultSubCommand {
    public ArenaRenameCommand() {
        super("rename", "Переименовать арену.", List.of(
                "sxpillars.commands.arena.rename"
        ));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }
}
