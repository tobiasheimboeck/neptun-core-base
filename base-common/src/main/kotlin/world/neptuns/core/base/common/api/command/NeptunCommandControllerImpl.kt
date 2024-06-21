package world.neptuns.core.base.common.api.command

import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandController
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.event.NeptunCommandRegisteredEvent
import world.neptuns.streamline.api.NeptunStreamlineProvider

class NeptunCommandControllerImpl : NeptunCommandController {

    override val commandExecutors: MutableMap<String, NeptunMainCommandExecutor> = mutableMapOf()
    override val commands: MutableList<NeptunCommand> = mutableListOf()

    override fun registerCommand(executor: NeptunMainCommandExecutor): NeptunCommand {
        val neptunCommandAnnotation = executor::class.java.getAnnotation(NeptunCommand::class.java)
            ?: throw NullPointerException("Command class needs to have to @NeptunCommand annotation!")

        this.commandExecutors[neptunCommandAnnotation.name] = executor
        this.commands.add(neptunCommandAnnotation)

        NeptunStreamlineProvider.api.eventController.fire(NeptunCommandRegisteredEvent(neptunCommandAnnotation))
        return neptunCommandAnnotation
    }

    override fun getCommandInitializer(name: String): NeptunMainCommandExecutor? {
        return this.commandExecutors.values.stream().filter { initializer: NeptunMainCommandExecutor? ->
            val properties = initializer?.javaClass?.getAnnotation(NeptunCommand::class.java)
            initializer!!.javaClass.getAnnotation(NeptunCommand::class.java) != null && name.isNotEmpty() && (properties?.name == name || properties?.aliases?.contains(name) ?: false)
        }.findFirst().orElse(null)
    }

    //    override fun registerCommand(executor: NeptunCommandExecutor): NeptunCommand {
//        val neptunCommand = executor::class.java.getAnnotation(NeptunCommand::class.java)
//            ?: throw NullPointerException("Command class needs to have to @NeptunCommand annotation!")
//
//        this.commandExecutors[neptunCommand.name] = executor
//        this.commands.add(neptunCommand)
//
//        NeptunStreamlineProvider.api.eventController.fire(NeptunCommandRegisteredEvent(neptunCommand))
//
//        return neptunCommand
//    }
//
//    override fun getCommandExecutor(name: String): NeptunCommandExecutor? {
//        return this.commandExecutors.values.stream().filter { handler: NeptunCommandExecutor? ->
//            val properties = handler?.javaClass?.getAnnotation(NeptunCommand::class.java)
//            handler!!.javaClass.getAnnotation(NeptunCommand::class.java) != null && name.isNotEmpty() && (properties?.name == name || properties?.aliases?.contains(
//                name
//            ) ?: false)
//        }.findFirst().orElse(null)
//    }

}