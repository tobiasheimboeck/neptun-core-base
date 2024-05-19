package world.neptuns.core.base.common.api.command

import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandController
import world.neptuns.core.base.api.command.NeptunCommandExecutor

class NeptunCommandControllerImpl : NeptunCommandController {

    override val commandExecutors: MutableMap<String, NeptunCommandExecutor> = mutableMapOf()
    override val commands: MutableList<NeptunCommand> = mutableListOf()

    override fun registerCommand(executor: NeptunCommandExecutor): NeptunCommand {
        val neptunCommand = executor::class.java.getAnnotation(NeptunCommand::class.java)
            ?: throw NullPointerException("Command class needs to have to @NeptunCommand annotation!")

        this.commandExecutors[neptunCommand.name] = executor
        this.commands.add(neptunCommand)

        return neptunCommand
    }

    override fun getCommandExecutor(name: String): NeptunCommandExecutor? {
        return this.commandExecutors.values.stream().filter { handler: NeptunCommandExecutor? ->
            val properties = handler?.javaClass?.getAnnotation(NeptunCommand::class.java)
            handler!!.javaClass.getAnnotation(NeptunCommand::class.java) != null && name.isNotEmpty() && (properties?.name == name || properties?.aliases?.contains(
                name
            ) ?: false)
        }.findFirst().orElse(null)
    }

}