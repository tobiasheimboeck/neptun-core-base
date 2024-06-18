package world.neptuns.core.base.api.command

import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

abstract class NeptunCommandInitializer {

    private val commandAnnotation: NeptunCommand? = this::class.java.getAnnotation(NeptunCommand::class.java)

    private val subCommandExecutors: MutableList<NeptunSubCommandExecutor> = mutableListOf()
    private val subCommands: MutableMap<NeptunSubCommand, NeptunSubCommandExecutor> = mutableMapOf()

    init {
        if (this.commandAnnotation == null) throw UnsupportedOperationException("NeptunCommandInitializer cannot be initiated! @NeptunCommand annotation is missing!")

        this.initSubCommands(this.subCommandExecutors)

        for (subCommand in subCommandExecutors) {
            val subCommandAnnotation = subCommand::class.java.getAnnotation(NeptunSubCommand::class.java)
                ?: throw UnsupportedOperationException("NeptunSubCommand cannot be initiated! @NeptunSubCommand annotation is missing!")

            this.subCommands[subCommandAnnotation] = subCommand
        }
    }

    abstract suspend fun defaultExecute(sender: NeptunCommandSender)
    abstract suspend fun onDefaultTabComplete(sender: NeptunCommandSender): List<String>

    abstract fun initSubCommands(subCommandExecutors: MutableList<NeptunSubCommandExecutor>)

    fun findValidSubCommandData(args: List<String>): Pair<NeptunSubCommandExecutor, NeptunSubCommand>? {
        var resultSubCommandExecutor: NeptunSubCommandExecutor? = null
        var subCommandResult: NeptunSubCommand? = null

        for (subCommand in this.subCommands) {
            val neptunSubCommand = subCommand.key
            val subCommandParts = neptunSubCommand.parts.split(" ")

            val isMatching = args.containsAll(subCommandParts)

            if (!isMatching || (neptunSubCommand.length != args.size && args.size <= neptunSubCommand.minLength))
                continue

            if (neptunSubCommand.minLength == -1 && (args.size > neptunSubCommand.length || args.size < neptunSubCommand.length))
                continue

            resultSubCommandExecutor = subCommand.value
            subCommandResult = subCommand.key
        }

        if (subCommandResult != null) println("args: $args | parts: ${subCommandResult.parts.split(" ")}")
        if (subCommandResult != null && !args.containsAll(subCommandResult.parts.split(" "))) return null

        return if (resultSubCommandExecutor == null || subCommandResult == null) null else resultSubCommandExecutor to subCommandResult
    }

}