package world.neptuns.core.base.api.command.extension

import world.neptuns.core.base.api.command.NeptunCommandCompletable

suspend fun NeptunCommandCompletable.generateSuggestions(args: List<String>, requiredLength: Int, valuesToCheck: List<Pair<Int, String>>, block: suspend MutableList<String>.() -> Unit): List<String> {
    return generateSuggestions(args, requiredLength, { a, b -> a == b }, valuesToCheck, block)
}

suspend fun NeptunCommandCompletable.generateSuggestions(args: List<String>, requiredLength: Int, comparison: (Int, Int) -> Boolean, valuesToCheck: List<Pair<Int, String>>, block: suspend MutableList<String>.() -> Unit): List<String> {
    var isFailed = false

    for (pair in valuesToCheck) {
        val pos = pair.first
        val string = pair.second

        if (args[pos].equals(string, true)) continue

        isFailed = true
    }

    if (isFailed) return emptyList()

    return generateSuggestions(args, requiredLength, comparison, block)
}

suspend fun NeptunCommandCompletable.generateSuggestions(args: List<String>, requiredLength: Int, block: suspend MutableList<String>.() -> Unit): List<String> {
    return generateSuggestions(args, requiredLength, { a, b -> a == b }, block)
}

suspend fun NeptunCommandCompletable.generateSuggestions(args: List<String>, requiredLength: Int, comparison: (Int, Int) -> Boolean, block: suspend MutableList<String>.() -> Unit): List<String> {
    if (!comparison(args.size, requiredLength)) return emptyList()

    val suggestions = mutableListOf<String>()

    block(suggestions)

    return suggestions.filter { it.regionMatches(0, args.last(), 0, args.last().length, ignoreCase = true) }
}

suspend fun NeptunCommandCompletable.generateSimpleSuggestions(args: List<String>, requiredLength: Int, block: suspend MutableList<String>.() -> Unit): List<String> {
    return generateSimpleSuggestions(args, requiredLength, { a, b -> a == b }, block)
}

suspend fun NeptunCommandCompletable.generateSimpleSuggestions(args: List<String>, requiredLength: Int, comparison: (Int, Int) -> Boolean, block: suspend MutableList<String>.() -> Unit): List<String> {
    if (!comparison(args.size, requiredLength)) return emptyList()

    val suggestions = mutableListOf<String>()

    block(suggestions)

    return suggestions
}