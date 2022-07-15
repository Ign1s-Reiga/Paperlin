package dev.reiga7953.spikotlin

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.java.JavaPlugin

fun CommandSender.execute(cmd: String) = Bukkit.dispatchCommand(this, cmd)

fun JavaPlugin.command(
    name: String, permission: String? = null, vararg aliases: String,
    executor: PluginCommand.(CommandSender, Array<String>) -> Unit
) = getCommand(name)!!.also {
    it.aliases = aliases.toList()
    it.setExecutor { sender, _, _, args ->
        it.executor(sender, args)
        true
    }
    it.permission = permission ?: return@also
}

fun JavaPlugin.command(
    name: String, permission: String? = null, vararg aliases: String,
    executor: CommandSender.(Array<String>) -> Unit
) = command(name, permission, *aliases) { sender, args -> sender.executor(args) }
