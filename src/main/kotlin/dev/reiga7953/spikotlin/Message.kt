package dev.reiga7953.spikotlin

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.FileWriter
import java.io.PrintWriter

fun JavaPlugin.info(msg: String?) = colored(msg, logger::info)
fun JavaPlugin.info(ex: Exception) = info(ex.message)

fun JavaPlugin.warning(msg: String?) = colored(msg, logger::warning)
fun JavaPlugin.warning(ex: Exception) = warning(ex.message)

fun JavaPlugin.severe(msg: String?) = colored(msg, logger::severe)
fun JavaPlugin.severe(ex: Exception) = severe(ex.message)

fun JavaPlugin.error(ex: Exception) {
    severe(ex.message ?: "&cAn internal error occured, check the logs");
    logToFile(ex)
}

fun JavaPlugin.logToFile(ex: Exception) = logToFile { ex.printStackTrace(this) }
fun JavaPlugin.logToFile(msg: String) = logToFile { println(msg) }

val JavaPlugin.logFile
    get() = dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun JavaPlugin.logToFile(action: PrintWriter.() -> Unit) =
    PrintWriter(FileWriter(logFile, true), true)
            .apply { print(currentDate); action() }.close()

fun CommandSender.msg(msg: String?) {
    try {
        if (msg != null) msg(textOf(msg))
    } catch (ex: Error) {
        colored(msg, ::sendMessage)
    }
}

fun CommandSender.msg(text: TextComponent) = spigot().sendMessage(text)
fun CommandSender.msg(ex: Exception) = msg(ex.message)

fun String.translateColorCode() = replace(Regex("&([A-Za-z0-9])")) { "§" + it.groups[1]!!.value }

fun textOf(string: String, builder: TextComponent.() -> Unit = {}) =
    TextComponent(*TextComponent.fromLegacyText(string.translateColorCode())).apply(builder)

class PluginException(msg: String) : Exception("&c$msg")

fun error(msg: String): Nothing = throw PluginException(msg)

fun colored(msg: String?, f: (String) -> Unit) {
    if (!msg.isNullOrBlank()) f(msg.translateColorCode())
}
