package world.neptuns.base.bukkit.api.extension

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch

private fun suspendToRunnable(plugin: SuspendingJavaPlugin, block: suspend () -> Unit) =
    Runnable { plugin.launch { block() } }

private fun suspendToAsynchronouslyRunnable(plugin: SuspendingJavaPlugin, block: suspend () -> Unit) =
    Runnable { plugin.launch(plugin.asyncDispatcher) { block() } }

fun runTask(plugin: SuspendingJavaPlugin, block: () -> Unit) = plugin.server.scheduler.runTask(plugin, block)

fun runSuspendTask(plugin: SuspendingJavaPlugin, block: suspend () -> Unit) = plugin.launch { block() }

fun runTaskAsynchronously(plugin: SuspendingJavaPlugin, block: () -> Unit) =
    plugin.server.scheduler.runTaskAsynchronously(plugin, block)

fun runSuspendTaskAsynchronously(plugin: SuspendingJavaPlugin, block: suspend () -> Unit) =
    plugin.launch(plugin.asyncDispatcher) { block() }

fun runTaskLater(plugin: SuspendingJavaPlugin, delay: Long, block: () -> Unit) =
    plugin.server.scheduler.runTaskLater(plugin, block, delay)

fun runSuspendTaskLater(plugin: SuspendingJavaPlugin, delay: Long, block: suspend () -> Unit) =
    plugin.server.scheduler.runTaskLater(plugin, suspendToRunnable(plugin, block), delay - 1L)

fun runTaskLaterAsynchronously(plugin: SuspendingJavaPlugin, delay: Long, block: () -> Unit) =
    plugin.server.scheduler.runTaskLaterAsynchronously(plugin, block, delay)

fun runSuspendTaskLaterAsynchronously(plugin: SuspendingJavaPlugin, delay: Long, block: suspend () -> Unit) =
    plugin.server.scheduler.runTaskLaterAsynchronously(plugin, suspendToAsynchronouslyRunnable(plugin, block), delay - 1L)

fun runTaskTimer(plugin: SuspendingJavaPlugin, delay: Long, period: Long, block: () -> Unit) =
    plugin.server.scheduler.runTaskTimer(plugin, block, delay, period)

fun runSuspendTaskTimer(plugin: SuspendingJavaPlugin, delay: Long, period: Long, block: suspend () -> Unit) =
    plugin.server.scheduler.runTaskTimer(plugin, suspendToRunnable(plugin, block), delay, period - 1L)

fun runTaskTimerAsynchronously(plugin: SuspendingJavaPlugin, delay: Long, period: Long, block: () -> Unit) =
    plugin.server.scheduler.runTaskTimerAsynchronously(plugin, block, delay, period)

fun runSuspendTaskTimerAsynchronously(plugin: SuspendingJavaPlugin, delay: Long, period: Long, block: suspend () -> Unit) =
    plugin.server.scheduler.runTaskTimerAsynchronously(plugin, suspendToAsynchronouslyRunnable(plugin, block), delay - 1L, period)