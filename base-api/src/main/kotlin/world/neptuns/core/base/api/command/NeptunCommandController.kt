package world.neptuns.core.base.api.command

interface NeptunCommandController {

    val commandInitializer: MutableMap<String, NeptunMainCommandExecutor>
    val commands: MutableList<NeptunCommand>

    fun registerCommand(initializer: NeptunMainCommandExecutor): NeptunCommand

    fun getCommandInitializer(name: String): NeptunMainCommandExecutor?

}