package world.neptuns.core.base.api.command

interface NeptunCommandController {

    val commandExecutors: MutableMap<String, NeptunCommandExecutor>
    val commands: MutableList<NeptunCommand>

    fun registerCommand(executor: NeptunCommandExecutor): NeptunCommand

    fun getCommandExecutor(name: String): NeptunCommandExecutor?

}