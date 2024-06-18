package world.neptuns.core.base.api.command

interface NeptunCommandController {

    val commandInitializer: MutableMap<String, NeptunCommandInitializer>
    val commands: MutableList<NeptunCommand>

    fun registerCommand(initializer: NeptunCommandInitializer): NeptunCommand

    fun getCommandInitializer(name: String): NeptunCommandInitializer?

}