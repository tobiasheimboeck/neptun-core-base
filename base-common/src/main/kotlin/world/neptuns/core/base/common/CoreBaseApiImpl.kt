package world.neptuns.core.base.common

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.SerializationCodec
import org.redisson.config.Config
import world.neptuns.core.base.api.CoreBaseApi
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandController
import world.neptuns.core.base.api.command.NeptunCommandExecutor
import world.neptuns.core.base.api.currency.CurrencyRegistry
import world.neptuns.core.base.api.file.FileController
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorController
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.api.player.NeptunPlayerController
import world.neptuns.core.base.api.player.PlayerAdapter
import world.neptuns.core.base.api.repository.RepositoryLoader
import world.neptuns.core.base.api.utils.PageConverter
import world.neptuns.core.base.common.api.command.NeptunCommandControllerImpl
import world.neptuns.core.base.common.api.file.FileControllerImpl
import world.neptuns.core.base.common.api.language.LangKeyImpl
import world.neptuns.core.base.common.api.language.LangNamespaceImpl
import world.neptuns.core.base.common.api.language.LanguageControllerImpl
import world.neptuns.core.base.common.api.language.LineKeyImpl
import world.neptuns.core.base.common.api.language.color.LanguageColorControllerImpl
import world.neptuns.core.base.common.api.language.color.LanguageColorImpl
import world.neptuns.core.base.common.api.language.properties.LanguagePropertiesControllerImpl
import world.neptuns.core.base.common.api.player.NeptunPlayerControllerImpl
import world.neptuns.core.base.common.api.repository.RepositoryLoaderImpl
import world.neptuns.core.base.common.api.utils.PageConverterImpl
import world.neptuns.core.base.common.file.MariaDbCredentials
import world.neptuns.core.base.common.file.RedisCredentials
import world.neptuns.core.base.common.repository.color.LanguageColorRepository
import world.neptuns.core.base.common.repository.color.LanguageColorTable
import world.neptuns.core.base.common.repository.language.LanguagePropertiesRepository
import world.neptuns.core.base.common.repository.language.LanguagePropertiesTable
import world.neptuns.core.base.common.repository.player.OfflinePlayerTable
import world.neptuns.core.base.common.repository.player.OnlinePlayerRepository
import java.nio.file.Path
import kotlin.coroutines.CoroutineContext

class CoreBaseApiImpl(override val minecraftDispatcher: CoroutineContext, override val dataFolder: Path) : CoreBaseApi {

    override val redissonClient: RedissonClient
    override val repositoryLoader: RepositoryLoader = RepositoryLoaderImpl()
    override val fileController: FileController = FileControllerImpl()
    override val playerController: NeptunPlayerController
    override val languageController: LanguageController = LanguageControllerImpl()
    override val languagePropertiesController: LanguagePropertiesController = LanguagePropertiesControllerImpl()
    override val languageColorController: LanguageColorController = LanguageColorControllerImpl()

    override val commandController: NeptunCommandController = NeptunCommandControllerImpl()
    private lateinit var commandExecutorClass: Class<*>

    init {
        establishMariaDbConnection(OfflinePlayerTable, LanguagePropertiesTable, LanguageColorTable)
        this.redissonClient = establishRedisConnection()

        this.repositoryLoader.register(OnlinePlayerRepository(redissonClient))
        this.repositoryLoader.register(LanguagePropertiesRepository(redissonClient))
        this.repositoryLoader.register(LanguageColorRepository(redissonClient))

        this.playerController = NeptunPlayerControllerImpl()
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

    override fun newLanguageColor(name: LineKey, permission: String?, hexFormat: String, description: LineKey, price: Long): LanguageColor {
        return LanguageColorImpl(name, permission, hexFormat, CurrencyRegistry.Default.CRYSTALS, description, price)
    }

    override fun <T> registerPlayerAdapter(playerAdapter: PlayerAdapter<T>) {
        TODO("Not yet implemented")
    }

    override fun <T> playerAdapter(clazz: Class<T>): PlayerAdapter<T> {
        TODO("Not yet implemented")
    }

    override fun <T> registerCommandExecutorClass(clazz: Class<T>) {
        this.commandExecutorClass = clazz
    }

    override fun registerCommand(commandExecutor: NeptunCommandExecutor) {
        val neptunCommand = this.commandController.registerCommand(commandExecutor)
        this.commandExecutorClass.getDeclaredConstructor(NeptunCommand::class.java).newInstance(neptunCommand)
    }

    override fun <T> newPageConverter(data: List<T>): PageConverter<T> {
        return PageConverterImpl(data)
    }

    private fun establishMariaDbConnection(vararg tables: Table) {
        val credentials = this.fileController.createOrLoadFile(this.dataFolder, "database", "mariadb", MariaDbCredentials::class, MariaDbCredentials(
            "127.0.0.1",
            3306,
            "neptunsworld_core",
            "root",
            "-"
        ))

        Database.connect(
            url = "jdbc:mariadb://${credentials.hostname}:${credentials.port}/${credentials.database}",
            driver = "org.mariadb.jdbc.Driver",
            user = credentials.user,
            password = credentials.password
        )

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(*tables)
        }
    }

    private fun establishRedisConnection(): RedissonClient {
        val credentials = this.fileController.createOrLoadFile(this.dataFolder, "database", "redis", RedisCredentials::class, RedisCredentials(
            "127.0.0.1",
            6379,
            "-"
        ))

        val config = Config()
        config.codec = SerializationCodec()
        config.nettyThreads = 4
        config.useSingleServer()
            .setAddress("redis://${credentials.hostname}:${credentials.port}")
            .setPassword(credentials.password)

        return Redisson.create(config)
    }

}