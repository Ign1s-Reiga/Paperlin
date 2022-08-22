package net.reiga7953.paperlin

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.FileWriter
import java.io.PrintWriter

fun JavaPlugin.info(msg: String?) = colored(msg, logger::info)
fun JavaPlugin.info(e: Exception) = info(e.message)

fun JavaPlugin.warning(msg: String?) = colored(msg, logger::warning)
fun JavaPlugin.warning(e: Exception) = warning(e.message)

fun JavaPlugin.severe(msg: String?) = colored(msg, logger::severe)
fun JavaPlugin.severe(e: Exception) = severe(e.message)

fun JavaPlugin.error(e: Exception) {
    severe(e.message ?: "&cAn internal error occured, check the logs");
    logToFile(e)
}

fun JavaPlugin.logToFile(e: Exception) = logToFile { e.printStackTrace(this) }
fun JavaPlugin.logToFile(msg: String) = logToFile { println(msg) }

val JavaPlugin.logFile
    get() = dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun JavaPlugin.logToFile(action: PrintWriter.() -> Unit) =
    PrintWriter(FileWriter(logFile, true), true)
            .apply { print(currentDate); action() }.close()

fun CommandSender.sendMessage(msg: String?) {
    try {
        if (msg != null) sendMessage(msg.translateColorCode())
    } catch (ex: Error) {
        colored(msg, ::sendMessage)
    }
}

fun CommandSender.sendMessage(e: Exception) = sendMessage(e.message)

fun String.translateColorCode() = replace(Regex("&([A-Za-z0-9])")) { "§" + it.groups[1]!!.value }

class PluginException(msg: String) : Exception("&c$msg")

fun error(msg: String): Nothing = throw PluginException(msg)

fun colored(msg: String?, f: (String) -> Unit) {
    if (!msg.isNullOrBlank()) f(msg.translateColorCode())
}
