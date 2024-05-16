package world.neptuns.core.base.api

import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.api.player.PlayerAdapter

interface CoreBaseApi {

    fun getLanguageController(): LanguageController

    fun getPlayerController(): NeptunPlayerController

    fun <P> registerPlayerAdapter(playerAdapter: PlayerAdapter<P>)

    fun <P> getPlayerAdapter(playerClass: Class<P>): PlayerAdapter<P>

}