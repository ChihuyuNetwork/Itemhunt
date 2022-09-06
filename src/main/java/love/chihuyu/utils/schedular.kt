package love.chihuyu.utils

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

inline fun bukkitRunnable(crossinline block: BukkitRunnable.() -> Unit) = object : BukkitRunnable() {
    override fun run() = block()
}

inline fun Plugin.runTask(crossinline block: BukkitRunnable.() -> Unit): BukkitTask {
    return bukkitRunnable(block).runTask(this)
}

inline fun Plugin.runTaskLater(delay: Long, crossinline block: BukkitRunnable.() -> Unit): BukkitTask {
    return bukkitRunnable(block).runTaskLater(this, delay)
}

inline fun Plugin.runTaskTimer(delay: Long, period: Long, crossinline block: BukkitRunnable.() -> Unit): BukkitTask {
    return bukkitRunnable(block).runTaskTimer(this, delay, period)
}

inline fun Plugin.runTaskTimerAsync(delay: Long, period: Long, crossinline block: BukkitRunnable.() -> Unit): BukkitTask {
    return bukkitRunnable(block).runTaskTimerAsynchronously(this, delay, period)
}

inline fun Plugin.runTaskAsync(crossinline block: BukkitRunnable.() -> Unit): BukkitTask {
    return bukkitRunnable(block).runTaskAsynchronously(this)
}