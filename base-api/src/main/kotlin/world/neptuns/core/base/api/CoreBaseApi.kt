package world.neptuns.core.base.api

import world.neptuns.core.base.api.command.NeptunCommandController
import world.neptuns.core.base.api.command.NeptunCommandInitializer
import world.neptuns.core.base.api.file.FileService
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorService
import world.neptuns.core.base.api.language.properties.LanguagePropertiesService
import world.neptuns.core.base.api.language.properties.default.DefaultLanguageProperties
import world.neptuns.core.base.api.player.NeptunPlayerService
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.utils.PageConverter
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext

interface CoreBaseApi {

    val minecraftDispatcher: CoroutineContext
    val dataFolder: Path

    val fileService: FileService

    val languageController: LanguageController
    val languagePropertiesService: LanguagePropertiesService
    val languageColorService: LanguageColorService
    val playerService: NeptunPlayerService

    val commandController: NeptunCommandController

    fun newLanguageKey(countryCode: String, languageCode: String): LangKey
    fun newNamespace(value: String, subPrefix: String?): LangNamespace
    fun newLineKey(namespace: LangNamespace, value: String): LineKey

    fun newLanguageColor(name: LineKey, permission: String?, hexFormat: String, price: Long): LanguageColor

    fun <T> registerPlayerAdapter(playerAdapter: PlayerAdapter<T>)
    fun <T> getPlayerAdapter(clazz: Class<T>): PlayerAdapter<T>

    fun <T> registerCommandExecutorClass(clazz: Class<T>)
    fun registerInitializer(initializer: NeptunCommandInitializer)

    fun <T> newPageConverter(data: List<T>): PageConverter<T>

    companion object {
        val defaultLangProperties = DefaultLanguageProperties()
    }

}