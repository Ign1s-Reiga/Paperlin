package dev.reiga7953.spikotlin

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.TimeUnit

fun JavaPlugin.cancelTasks() = server.scheduler.cancelTasks(this)

fun JavaPlugin.schedule(
    async: Boolean = false,
    sdelay: Long = 0,
    speriod: Long = 0,
    unit: TimeUnit = TimeUnit.SECONDS,
    callback: BukkitTask.() -> Unit
): BukkitTask = let { plugin ->
    lateinit var task: BukkitTask
    fun f() = task.callback()
    task = server.scheduler.run {
        val sdelay = unit.toSeconds(sdelay) * 20
        val speriod = unit.toSeconds(speriod) * 20
        when {
            speriod > 0 -> {
                when {
                    async -> runTaskTimerAsynchronously(plugin, ::f, sdelay, speriod)
                    else -> runTaskTimer(plugin, ::f, sdelay, speriod)
                }
            }
            sdelay > 0 -> {
                when {
                    async -> runTaskLaterAsynchronously(plugin, ::f, sdelay)
                    else -> runTaskLater(plugin, ::f, sdelay)
                }
            }
            async -> runTaskAsynchronously(plugin, ::f)
            else -> runTask(plugin, ::f)
        }
    }
    return task
}
