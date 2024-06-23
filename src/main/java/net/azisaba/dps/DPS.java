package net.azisaba.dps;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class DPS {

    private static final Multimap<UUID, DamageData> DAMAGES = HashMultimap.create();

    private record DamageData(UUID uuid, Double damage) {}

    private static final Multimap<UUID, TimeData> TIMES = HashMultimap.create();
    private record TimeData(UUID uuid, int seconds) {}

    public static void start(UUID player, UUID victim) {
        for (TimeData d : TIMES.get(player).stream().toList()) {
            if (d.uuid() != victim) continue;
            return;
        }
        TimeData data = new TimeData(victim, 1);
        TIMES.put(player, data);
        SpigotPlugin.getInstance().runAsyncDelayed(()-> loop(player, data), 20);
    }

    private static void loop(UUID player, @NotNull TimeData data) {
        AtomicBoolean f = new AtomicBoolean(false);
        SpigotPlugin.getInstance().runSync(()-> {
            Entity entity = Bukkit.getEntity(data.uuid());
            if (entity == null) {
                remove(data.uuid());
                f.set(true);
            }
        });

        if (f.get()) return;
        List<TimeData> list = new ArrayList<>();
        int i = 1;
        for (TimeData d : TIMES.get(player).stream().toList()) {
            if (d.uuid() != data.uuid()) continue;
            list.add(d);
            i+= d.seconds();
        }
        list.forEach(l -> TIMES.remove(player, l));
        TimeData renew = new TimeData(data.uuid(), i);
        TIMES.put(player, renew);

        SpigotPlugin.getInstance().runAsyncDelayed(()-> loop(player, renew), 20);
    }

    public static void add(UUID player, UUID victim, Double damage) {
        double v = 0;
        List<DamageData> list = new ArrayList<>();
        for (DamageData d : DAMAGES.get(player).stream().toList()) {
            if (d.uuid() != victim) continue;
            v+= d.damage();
            list.add(d);
        }
        list.forEach(l -> DAMAGES.remove(player, l));
        DAMAGES.put(player, new DamageData(victim, damage + v));
    }

    public void clear(UUID player) {
        DAMAGES.removeAll(player);
        TIMES.removeAll(player);
    }

    public static String getDamage(UUID player, UUID victim) {
        double v = 0;
        for (DamageData d : DAMAGES.get(player).stream().toList()) {
            if (d.uuid() != victim) continue;
            v+= d.damage();
        }

        int s = 0;
        for (TimeData t : TIMES.get(player).stream().toList()) {
            if (t.uuid()!= victim) continue;
            s+= t.seconds();
        }

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(v / s);
    }

    public static void remove(UUID victim) {
        for (UUID key : DAMAGES.keySet()) {
            for (DamageData d : DAMAGES.get(key).stream().toList()) {
                if (d.uuid() == victim) DAMAGES.remove(key, d);
            }
        }
        for (UUID key : TIMES.keySet()) {
            for (TimeData d : TIMES.get(key).stream().toList()) {
                if (d.uuid() == victim) TIMES.remove(key, d);
            }
        }
    }
}
