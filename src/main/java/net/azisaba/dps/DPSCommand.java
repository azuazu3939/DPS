package net.azisaba.dps;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DPSCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player p)) return false;
        if (strings.length != 1) {
            sendMessage(p);
            return true;
        }
        switch (strings[0]) {
            case "on":
                new ShowDPS().on(p);
                p.sendMessage(Component.text("DPSの表示をonにしました。"));
                break;
            case "off":
                new ShowDPS().off(p);
                p.sendMessage(Component.text("DPSの表示をoffにしました。"));
                break;
            case "clear":
                new DPS().clear(p.getUniqueId());
                p.sendMessage(Component.text("DPSの履歴を全て消しました。"));
                break;
            default:
                sendMessage(p);
                return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 1) {
            return List.of("on", "off", "clear");
        }
        return null;
    }

    private void sendMessage(@NotNull Player p) {
        p.sendMessage(Component.text("/dps [on, off, clear]"));
    }
}
