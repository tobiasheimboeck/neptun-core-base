package world.neptuns.core.base.api.command

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE)
annotation class NeptunCommand(
    val type: NeptunCommandPlatform,
    val name: String,
    val permission: String = "",
    val aliases: Array<String> = []
)
