package de.lachcrafter.lachshield.scheduling;

import de.lachcrafter.lachshield.LachShield;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.scheduler.BukkitScheduler;

public class SchedulerFactory {
    private final LachShield plugin;
    private final GlobalRegionScheduler foliaScheduler;
    private final BukkitScheduler bukkitScheduler;
    private final static boolean IS_FOLIA = LachShield.isFolia();

    public SchedulerFactory(LachShield plugin) {
        this.plugin = plugin;

        foliaScheduler = plugin.getServer().getGlobalRegionScheduler();
        bukkitScheduler = plugin.getServer().getScheduler();
    }

    /**
     * Schedule a task in ticks.
     * @param ticks Time that should pass until the task should execute.
     * @param task The task to run.
     */
    public void scheduleInTicks(int ticks, ScheduleTask task) {
        if (IS_FOLIA) {
            foliaScheduler.runDelayed(plugin, (scheduledTask -> task.run()), ticks);
        } else {
            bukkitScheduler.runTaskLater(plugin, task::run, ticks);
        }
    }

    /**
     * Schedule a task in ticks that gets repeated with a delay.
     * @param startTicks Time that should pass until the task should execute.
     * @param repeatDelayTicks Delay in ticks until the task gets repeated.
     * @param task The task to run.
     */
    public void scheduleRepeatable(int startTicks, int repeatDelayTicks, ScheduleTask task) {
        if (IS_FOLIA) {
            foliaScheduler.runAtFixedRate(plugin, (scheduledTask -> task.run()), startTicks, repeatDelayTicks);
        } else {
            bukkitScheduler.runTaskTimer(plugin, task::run, startTicks, repeatDelayTicks);
        }
    }

}
