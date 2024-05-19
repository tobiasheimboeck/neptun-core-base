package world.neptuns.core.base.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.redisson.api.RedissonClient
import world.neptuns.core.base.api.command.NeptunCommandController
import world.neptuns.core.base.api.command.NeptunCommandExecutor
import world.neptuns.core.base.api.file.FileController
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorController
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.repository.RepositoryLoader
import world.neptuns.core.base.api.utils.PageConverter
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext

interface CoreBaseApi {

    val minecraftDispatcher: CoroutineContext
    val dataFolder: Path
    val redissonClient: RedissonClient

    val repositoryLoader: RepositoryLoader

    val fileController: FileController
    val playerController: NeptunPlayerController

    val languageController: LanguageController
    val languagePropertiesController: LanguagePropertiesController
    val languageColorController: LanguageColorController

    val commandController: NeptunCommandController

    fun newLanguageKey(countryCode: String, languageCode: String): LangKey
    fun newLineKey(namespace: String, value: String): LineKey
    fun newLanguageColor(name: LineKey, permission: String?, hexFormat: String, description: LineKey, price: Long): LanguageColor

    fun <T> registerPlayerAdapter(playerAdapter: PlayerAdapter<T>)
    fun <T> playerAdapter(clazz: Class<T>): PlayerAdapter<T>

    fun <T> registerCommandExecutorClass(clazz: Class<T>)
    fun registerCommand(commandExecutor: NeptunCommandExecutor)

    fun <T> newPageConverter(data: List<T>): PageConverter<T>

    companion object {
        val GSON: Gson = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
    }

}