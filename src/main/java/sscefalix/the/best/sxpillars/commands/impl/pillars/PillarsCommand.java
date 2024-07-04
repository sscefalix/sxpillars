package sscefalix.the.best.sxpillars.commands.impl.pillars;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sscefalix.the.best.sxpillars.SXPillars;
import sscefalix.the.best.sxpillars.commands.DefaultCommand;
import sscefalix.the.best.sxpillars.managers.ColorManager;
import sscefalix.the.best.sxpillars.managers.database.records.PlayerStats;

import java.util.List;

public class PillarsCommand extends DefaultCommand {
    public PillarsCommand() {
        super("pillars", "Команда для управления плагином.", List.of(
                "sxpillars.commands.pillars"
        ), List.of(
                "&7&m&l--------&r&e Команды администратора &7&m&l--------",
                "&0",
                "&e● &7Создать арену — &e/pillars reload",
                "&0",
                "&7&m&l------------&r&e By &b&lsscefalix &c❤ &7&m&l------------"
        ));

        addSubCommand(new PillarsReloadCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ColorManager.colorize("&cКоманду может выполнить только игрок."));
            return;
        }

        PlayerStats stats = SXPillars.getInstance().getDatabase().getPlayerStats(player);

        player.sendMessage(ColorManager.colorize("&0 "));
        player.sendMessage(ColorManager.colorize("&eВаша статистика: "));
        player.sendMessage(ColorManager.colorize("&cУбийств &f— &c" + stats.kills()));
        player.sendMessage(ColorManager.colorize("&eСмертей &f— &e" + stats.deaths()));
        player.sendMessage(ColorManager.colorize("&aПобед &f— &a" + stats.wins()));
        player.sendMessage(ColorManager.colorize("&4Поражений &f— &4" + stats.losses()));
        player.sendMessage(ColorManager.colorize("&0 "));
    }
}
