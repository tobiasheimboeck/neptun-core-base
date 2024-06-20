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

        for ((neptunSubCommand, subCommandExecutor) in this.subCommands) {
            val subCommandParts = neptunSubCommand.parts.split(" ")

            val correctedSubCommandParts = subCommandParts.filter { !it.contains("{") && !it.contains("}") }

            val isMatching = args.containsAll(correctedSubCommandParts)

            println("isMatching: $isMatching >> $args | $correctedSubCommandParts")

            if (!isMatching) continue

            if (neptunSubCommand.length != -1 && args.size != neptunSubCommand.length) continue

            if (neptunSubCommand.minLength != -1 && args.size < neptunSubCommand.minLength) continue

            println("HELLO WORLD ::: ${args.size}:${neptunSubCommand.length}")

            resultSubCommandExecutor = subCommandExecutor
            subCommandResult = neptunSubCommand
            break
        }

        return if (resultSubCommandExecutor == null || subCommandResult == null) null else resultSubCommandExecutor to subCommandResult
    }

//    fun findValidSubCommandData(args: List<String>): Pair<NeptunSubCommandExecutor, NeptunSubCommand>? {
//        var resultSubCommandExecutor: NeptunSubCommandExecutor? = null
//        var subCommandResult: NeptunSubCommand? = null
//
//        for (subCommand in this.subCommands) {
//            val neptunSubCommand = subCommand.key
//            val subCommandParts = neptunSubCommand.parts.split(" ")
//
//            val correctedSubCommandParts = subCommandParts.toMutableList()
//            correctedSubCommandParts.removeIf() { it.contains("{") || it.contains("}") }
//
//            val isMatching = args.containsAll(correctedSubCommandParts)
//
//            println("isMatching: $isMatching >> $args | $correctedSubCommandParts")
//
//            if (!isMatching || (neptunSubCommand.length != args.size && args.size <= neptunSubCommand.minLength))
//                continue
//
//            if (neptunSubCommand.minLength == -1 && (args.size > neptunSubCommand.length || args.size < neptunSubCommand.length))
//                continue
//
//            println("HELLO WORLD ::: ${args.size}:${neptunSubCommand.length}")
//
//            resultSubCommandExecutor = subCommand.value
//            subCommandResult = subCommand.key
//        }
//
//        return if (resultSubCommandExecutor == null || subCommandResult == null) null else resultSubCommandExecutor to subCommandResult
//    }

}