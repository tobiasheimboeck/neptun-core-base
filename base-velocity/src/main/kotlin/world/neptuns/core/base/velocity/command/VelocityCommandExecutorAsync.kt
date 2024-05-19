package world.neptuns.core.base.velocity.command

import com.github.shynixn.mccoroutine.velocity.SuspendingSimpleCommand
import com.github.shynixn.mccoroutine.velocity.registerSuspend
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.velocity.NeptunVelocityPlugin

class VelocityCommandExecutorAsync(private val neptunCommand: NeptunCommand) : SuspendingSimpleCommand {

    private val neptunCommandExecutor = NeptunCoreProvider.api.commandController.getCommandExecutor(this.neptunCommand.name)!!

    init {
        val proxyServer = NeptunVelocityPlugin.instance.proxyServer
        val commandManager = proxyServer.commandManager
        commandManager.registerSuspend(commandManager.metaBuilder(this.neptunCommand.name).aliases(*this.neptunCommand.aliases).build(), this, proxyServer)
    }

    override suspend fun execute(invocation: SimpleCommand.Invocation) {
        val sender = invocation.source()
        val neptunCommandSender = VelocityCommandSender(sender)

        if (checkPermission(neptunCommandSender, sender)) {
            //TODO: Send no permission warning
            return
        }

        this.neptunCommandExecutor.execute(neptunCommandSender, invocation.arguments().toList())
    }

    override suspend fun suggest(invocation: SimpleCommand.Invocation): List<String> {
        val sender = invocation.source()
        val neptunCommandSender = VelocityCommandSender(sender)

        if (checkPermission(neptunCommandSender, sender)) return emptyList()
        return this.neptunCommandExecutor.onTabComplete(neptunCommandSender, invocation.arguments().toList())
    }

    private fun checkPermission(neptunCommandSender: NeptunCommandSender, sender: CommandSource): Boolean {
        return neptunCommandSender.isPlayer() && (this.neptunCommand.permission != "") && (sender.hasPermission(this.neptunCommand.permission))
    }

}