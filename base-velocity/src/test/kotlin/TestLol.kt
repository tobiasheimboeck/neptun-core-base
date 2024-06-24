
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.SerializationCodec
import org.redisson.config.Config
import world.neptuns.streamline.common.NeptunStreamlineConnector
import java.util.*
import kotlin.test.assertTrue

object TestLol {

    private lateinit var redissonClient: RedissonClient

    @BeforeEach
    fun init(): Unit = runBlocking {
        val redisHostname = "37.114.34.73"
        val redisPort = 6379
        val redisPassword = "FxEjrfDu3Z6wP9dza8AU5b"

        val config = Config()
        config.codec = SerializationCodec()
        config.nettyThreads = 4
        config.useSingleServer()
            .setAddress("redis://$redisHostname:$redisPort")
            .setPassword(redisPassword)

        redissonClient = Redisson.create(config)

        val password = "K{T4?f,AmV${"$"}${"p"}/U3Q+_%g^@"

        Database.connect(
            url = "jdbc:mariadb://37.114.34.73:3306/neptunsworld_core",
            driver = "org.mariadb.jdbc.Driver",
            user = "root",
            password = password
        )

        NeptunStreamlineConnector.init(redissonClient)
    }

    @Test
    fun testDb() = runBlocking {
        newSuspendedTransaction {
            OfflinePlayerTable.insert {
                it[this.uuid] = UUID.fromString("7bcaba2a-0fdf-49ab-b235-e5821d04c367")

                                            //          7bcaba2a-0fdf-49ab-b235-e5821d04c367

                it[this.name] = "TGamings"
                it[this.firstLoginTimestamp] = System.currentTimeMillis()
                it[this.lastLoginTimestamp] = System.currentTimeMillis()
                it[this.lastLogoutTimestamp] = System.currentTimeMillis()
                it[this.onlineTime] = System.currentTimeMillis()
                it[this.crystals] = 0L
                it[this.shards] = 0L
                it[this.skinValue] = "hello"
                it[this.skinSignature] = "world"
            }
        }

        assertTrue(true)
    }

}