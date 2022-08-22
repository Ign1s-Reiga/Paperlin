package net.reiga7953.paperlin

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

inline fun <reified T : Event> JavaPlugin.listen(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: (T) -> Unit
) = server.pluginManager.registerEvent(
    T::class.java, object : Listener {},
    priority, { _, it -> if (it is T) callback(it) },
    this, ignoreCancelled
)
