package world.neptuns.core.base.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.redisson.api.RedissonClient
import world.neptuns.core.base.api.file.FileController
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.LanguageKey
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

    fun newLanguageKey(countryCode: String, languageCode: String): LanguageKey
    fun getLanguageController(): LanguageController
    fun getLanguagePropertiesController(): LanguagePropertiesController

    fun <T> registerPlayerAdapter(playerAdapter: PlayerAdapter<T>)
    fun <T> playerAdapter(clazz: Class<T>): PlayerAdapter<T>
    fun <T> newPageConverter(data: List<T>): PageConverter<T>

    companion object {
        val GSON: Gson = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
    }

}