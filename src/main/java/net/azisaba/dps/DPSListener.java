package net.azisaba.dps;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

public class DPSListener implements Listener {

    @EventHandler
    public void onDeath(@NotNull EntityDeathEvent e) {
        LivingEntity living = e.getEntity();
        if (living instanceof Player) return;
        SpigotPlugin.getInstance().runAsync(()-> DPS.remove(living.getUniqueId()));
    }

    @EventHandler
    public void onDamage(@NotNull EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        if (!(e.getEntity() instanceof LivingEntity living)) return;
        if (!ShowDPS.isOn(p)) return;
        SpigotPlugin.getInstance().runAsync(()-> {
            DPS.add(p.getUniqueId(), living.getUniqueId(), e.getDamage());
            DPS.start(p.getUniqueId(), living.getUniqueId());

            ShowDPS.show(p, DPS.getDamage(p.getUniqueId(), living.getUniqueId()));
        });
    }
}
