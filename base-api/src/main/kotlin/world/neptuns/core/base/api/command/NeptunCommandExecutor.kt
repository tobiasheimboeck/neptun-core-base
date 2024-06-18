package world.neptuns.core.base.api.command

interface NeptunCommandExecutor {

    suspend fun execute(sender: NeptunCommandSender, args: List<String>)

    fun sendUsage(sender: NeptunCommandSender)

    suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String>

}