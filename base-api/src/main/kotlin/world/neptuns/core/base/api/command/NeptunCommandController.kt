package world.neptuns.core.base.api.command

interface NeptunCommandController {

    val commandExecutors: MutableMap<String, NeptunMainCommandExecutor>
    val commands: MutableList<NeptunCommand>

    fun registerCommand(executor: NeptunMainCommandExecutor): NeptunCommand

    fun getCommandInitializer(name: String): NeptunMainCommandExecutor?

}