package world.neptuns.core.base.common

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import world.neptuns.controller.api.NeptunControllerProvider
import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandController
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.currency.CurrencyRegistry
import world.neptuns.core.base.api.file.FileService
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorService
import world.neptuns.core.base.api.language.properties.LanguagePropertiesService
import world.neptuns.core.base.api.player.NeptunPlayerService
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.util.PageConverter
import world.neptuns.core.base.common.api.command.NeptunCommandControllerImpl
import world.neptuns.core.base.common.api.file.FileControllerImpl
import world.neptuns.core.base.common.api.language.LangKeyImpl
import world.neptuns.core.base.common.api.language.LangNamespaceImpl
import world.neptuns.core.base.common.api.language.LanguageControllerImpl
import world.neptuns.core.base.common.api.language.LineKeyImpl
import world.neptuns.core.base.common.api.language.color.LanguageColorImpl
import world.neptuns.core.base.common.api.language.color.LanguageColorServiceImpl
import world.neptuns.core.base.common.api.language.properties.LanguagePropertiesServiceImpl
import world.neptuns.core.base.common.api.player.NeptunPlayerServiceImpl
import world.neptuns.core.base.common.api.utils.PageConverterImpl
import world.neptuns.core.base.common.repository.color.LanguageColorCache
import world.neptuns.core.base.common.repository.color.LanguageColorRepository
import world.neptuns.core.base.common.repository.color.LanguageColorTable
import world.neptuns.core.base.common.repository.language.LanguagePropertiesCache
import world.neptuns.core.base.common.repository.language.LanguagePropertiesRepository
import world.neptuns.core.base.common.repository.language.LanguagePropertiesTable
import world.neptuns.core.base.common.repository.player.OfflinePlayerTable
import world.neptuns.core.base.common.repository.player.OnlinePlayerCache
import world.neptuns.core.base.common.repository.player.OnlinePlayerRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext

class CoreBaseApiImpl(override val minecraftDispatcher: CoroutineContext, override val dataFolder: Path) : CoreBaseApi {

    override val fileService: FileService = FileControllerImpl()
    override val languageController: LanguageController = LanguageControllerImpl()

    override val languageColorService: LanguageColorService
    override val languagePropertiesService: LanguagePropertiesService
    override val playerService: NeptunPlayerService

    private lateinit var playerAdapter: PlayerAdapter

    override val commandController: NeptunCommandController = NeptunCommandControllerImpl()
    private lateinit var commandExecutorClass: Class<*>

    init {
        val redissonClient = NeptunControllerProvider.api.redissonClient

        transaction {
            SchemaUtils.create(LanguageColorTable, LanguagePropertiesTable, OfflinePlayerTable)
        }

        val repositoryLoader = NeptunStreamlineProvider.api.repositoryLoader
        repositoryLoader.register(OnlinePlayerRepository(redissonClient))
        repositoryLoader.register(LanguagePropertiesRepository(redissonClient))
        repositoryLoader.register(LanguageColorRepository(redissonClient))

        val cacheLoader = NeptunStreamlineProvider.api.cacheLoader
        cacheLoader.register(OnlinePlayerCache())
        cacheLoader.register(LanguageColorCache())
        cacheLoader.register(LanguagePropertiesCache())

        this.languageColorService = LanguageColorServiceImpl()
        this.languagePropertiesService = LanguagePropertiesServiceImpl()
        this.playerService = NeptunPlayerServiceImpl()
    }

    override fun newLanguageKey(countryCode: String, languageCode: String): LangKey {
        return LangKeyImpl(countryCode, languageCode)
    }

    override fun newNamespace(value: String, subPrefix: String?): LangNamespace {
        return LangNamespaceImpl(value, subPrefix)
    }

    override fun newLineKey(namespace: LangNamespace, value: String): LineKey {
        return LineKeyImpl(namespace, value)
    }

    override fun newLanguageColor(name: LineKey, permission: String?, hexFormat: String, price: Long): LanguageColor {
        return LanguageColorImpl(name, permission, hexFormat, CurrencyRegistry.Default.CRYSTALS, price)
    }

    override fun registerPlayerAdapter(playerAdapter: PlayerAdapter) {
        this.playerAdapter = playerAdapter
    }

    override fun getPlayerAdapter(): PlayerAdapter {
        return this.playerAdapter
    }

    override fun <T> registerCommandExecutorClass(clazz: Class<T>) {
        this.commandExecutorClass = clazz
    }

    override fun registerInitializer(initializer: NeptunMainCommandExecutor) {
        val neptunCommand = this.commandController.registerCommand(initializer)
        this.commandExecutorClass.getDeclaredConstructor(NeptunCommand::class.java).newInstance(neptunCommand)
    }

    override fun <T> newPageConverter(data: List<T>): PageConverter<T> {
        return PageConverterImpl(data)
    }

}