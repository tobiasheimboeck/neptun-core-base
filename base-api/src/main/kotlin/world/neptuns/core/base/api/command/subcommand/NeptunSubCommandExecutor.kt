package world.neptuns.core.base.api.command.subcommand

import world.neptuns.core.base.api.command.NeptunCommandCompletable
import world.neptuns.core.base.api.command.NeptunCommandSender

interface NeptunSubCommandExecutor : NeptunCommandCompletable {

    suspend fun execute(sender: NeptunCommandSender, args: List<String>)

    suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String>

}