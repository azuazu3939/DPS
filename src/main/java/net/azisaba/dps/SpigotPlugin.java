package net.azisaba.dps;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SpigotPlugin extends JavaPlugin {

    private static SpigotPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        Objects.requireNonNull(getCommand("dps")).setExecutor(new DPSCommand());

        getServer().getPluginManager().registerEvents(new DPSListener(), this);
    }

    public void runAsync(Runnable runnable) {Bukkit.getScheduler().runTaskAsynchronously(this, runnable);}

    public void runSync(Runnable runnable) {Bukkit.getScheduler().runTask(this, runnable);}

    public void runAsyncDelayed(Runnable runnable, long delay) {Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, delay);}

    public static SpigotPlugin getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
