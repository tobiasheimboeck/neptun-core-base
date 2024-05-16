package world.neptuns.core.base.common

import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.repository.RepositoryLoader
import world.neptuns.core.base.common.api.player.NeptunPlayerControllerImpl
import world.neptuns.core.base.common.api.repository.RepositoryLoaderImpl
import world.neptuns.core.base.common.repository.OnlinePlayerRepository
import kotlin.coroutines.CoroutineContext

class CoreBaseApiImpl(override val minecraftDispatcher: CoroutineContext) : CoreBaseApi {

    override val repositoryLoader: RepositoryLoader = RepositoryLoaderImpl()
    override val playerController: NeptunPlayerController

    init {
        this.repositoryLoader.register(OnlinePlayerRepository(NeptunControllerProvider.api.redissonClient))
        this.playerController = NeptunPlayerControllerImpl()
    }

    override fun getLanguageController(): LanguageController {
        TODO("Not yet implemented")
    }

    override fun <T> registerPlayerAdapter(playerAdapter: PlayerAdapter<T>) {
        TODO("Not yet implemented")
    }

    override fun <T> getPlayerAdapter(clazz: Class<T>): PlayerAdapter<T> {
        TODO("Not yet implemented")
    }

}