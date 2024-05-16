package world.neptuns.core.base.api

import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.repository.RepositoryLoader
import kotlin.coroutines.CoroutineContext

interface CoreBaseApi {

    val minecraftDispatcher: CoroutineContext
    val repositoryLoader: RepositoryLoader
    val playerController: NeptunPlayerController

    fun getLanguageController(): LanguageController

    fun <T> registerPlayerAdapter(playerAdapter: PlayerAdapter<T>)
    fun <T> getPlayerAdapter(clazz: Class<T>): PlayerAdapter<T>

}