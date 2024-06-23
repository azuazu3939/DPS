package net.azisaba.dps;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class ShowDPS {

    private static final Set<String> PLAYERS = new HashSet<>();

    public void on(@NotNull Player p) {
        PLAYERS.add(p.getName());
    }

    public void off(@NotNull Player p) {PLAYERS.remove(p.getName());}

    public static boolean isOn(@NotNull Player p) {return PLAYERS.contains(p.getName());}

    public static void show(@NotNull Player p, String damage) {
        Component comp = Component.text("                                              DPS " + damage, NamedTextColor.WHITE).decorate(TextDecoration.BOLD);
        Title.Times times = Title.Times.times(Duration.ofMillis(0), Duration.ofMillis(10000), Duration.ofMillis(0));
        Title title = Title.title(Component.empty(), comp, times);
        p.showTitle(title);
    }
}
