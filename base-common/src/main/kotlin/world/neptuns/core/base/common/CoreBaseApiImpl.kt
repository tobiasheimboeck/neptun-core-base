package world.neptuns.core.base.common

import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.api.player.PlayerAdapter
import kotlin.coroutines.CoroutineContext

class CoreBaseApiImpl(override val minecraftDispatcher: CoroutineContext) : CoreBaseApi {

    override fun getLanguageController(): LanguageController {
        TODO("Not yet implemented")
    }

    override fun getPlayerController(): NeptunPlayerController {
        TODO("Not yet implemented")
    }

    override fun <T> registerPlayerAdapter(playerAdapter: PlayerAdapter<T>) {
        TODO("Not yet implemented")
    }

    override fun <T> getPlayerAdapter(clazz: Class<T>): PlayerAdapter<T> {
        TODO("Not yet implemented")
    }

}