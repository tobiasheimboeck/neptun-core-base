package world.neptuns.core.base.api.command

import java.util.*
import java.util.stream.Collectors

interface NeptunCommandExecutor {

    suspend fun execute(sender: NeptunCommandSender, args: List<String>)

    fun sendUsage(sender: NeptunCommandSender)

    suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String>

    fun equals(element: String, value: String?): Boolean {
        return element.equals(value, ignoreCase = true)
    }

    fun equals(vararg toCheck: Pair<String, String>): Boolean {
        return Arrays.stream(toCheck).allMatch { pair -> pair.first == pair.second }
    }

    fun equals(args: List<String>, requiredLength: Int, vararg toCheck: Pair<String, String>): Boolean {
        return args.size == requiredLength && Arrays.stream(toCheck).allMatch { pair -> pair.first == pair.second }
    }

    fun filter(stringList: List<String>, argument: String): List<String> {
        return stringList.stream().filter { s: String -> s.startsWith(argument) }.collect(Collectors.toList())
    }

}