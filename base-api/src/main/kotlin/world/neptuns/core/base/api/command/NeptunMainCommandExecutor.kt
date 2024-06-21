package world.neptuns.core.base.api.command

import world.neptuns.core.base.api.command.subcommand.NeptunSubCommand
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor

abstract class NeptunMainCommandExecutor : NeptunCommandCompletable {

    private val commandAnnotation: NeptunCommand? = this::class.java.getAnnotation(NeptunCommand::class.java)

    val subCommandExecutors: MutableList<NeptunSubCommandExecutor> = mutableListOf()
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
    abstract suspend fun onDefaultTabComplete(sender: NeptunCommandSender, args: List<String>): List<String>

    abstract fun initSubCommands(subCommandExecutors: MutableList<NeptunSubCommandExecutor>)

    fun findSubCommandParts(): List<String> {
        val subCommandParts = mutableListOf<String>()

        for ((neptunSubCommand, _) in this.subCommands) {
            subCommandParts.add(neptunSubCommand.parts)
        }

        return subCommandParts
    }

    fun findValidSubCommandData(args: List<String>): Pair<NeptunSubCommandExecutor, NeptunSubCommand>? {
        var resultSubCommandExecutor: NeptunSubCommandExecutor? = null
        var subCommandResult: NeptunSubCommand? = null

        for ((neptunSubCommand, subCommandExecutor) in this.subCommands) {
            val subCommandParts = neptunSubCommand.parts.split(" ")

            val correctedSubCommandParts = subCommandParts.filter { !it.contains("<") && !it.contains(">") && !it.contains("[") && !it.contains("]") }

            if (!args.containsAll(correctedSubCommandParts)) continue
            if (neptunSubCommand.length != -1 && args.size != neptunSubCommand.length) continue
            if ((neptunSubCommand.minLength != -1 && neptunSubCommand.maxLength != -1) && (args.size < neptunSubCommand.minLength || args.size > neptunSubCommand.maxLength)) continue

            resultSubCommandExecutor = subCommandExecutor
            subCommandResult = neptunSubCommand
            break
        }

        return if (resultSubCommandExecutor == null || subCommandResult == null) null else Pair(resultSubCommandExecutor, subCommandResult)
    }

}