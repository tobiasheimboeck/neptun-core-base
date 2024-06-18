package world.neptuns.core.base.api.command.subcommand

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class NeptunSubCommand(
    val length: Int = -1, // -1 means: value is not used
    val minLength: Int = -1,
    val parts: String,
    val permission: String = "",
)