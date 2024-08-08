package world.neptuns.core.base.bukkit.api.location

import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import world.neptuns.base.bukkit.api.location.WorldLocation
import world.neptuns.base.bukkit.api.location.WorldLocationService
import world.neptuns.core.base.bukkit.database.location.WorldLocationTable
import world.neptuns.core.base.bukkit.database.location.dao.WorldLocationEntity
import world.neptuns.core.base.bukkit.repository.location.WorldLocationCache
import world.neptuns.core.base.bukkit.repository.location.WorldLocationRepository
import world.neptuns.streamline.api.NeptunStreamlineProvider

@Suppress("OPT_IN_USAGE")
class WorldLocationServiceImpl : WorldLocationService {

    private val locationRepository = NeptunStreamlineProvider.api.repositoryLoader.get(WorldLocationRepository::class.java)!!
    private val locationCache = NeptunStreamlineProvider.api.cacheLoader.get(WorldLocationCache::class.java)!!

    init {
        GlobalScope.launch(Dispatchers.IO) {
            locationRepository.onInsertion { locationName, location ->
                if (locationCache.contains(locationName)) return@onInsertion
                locationCache.insert(locationName, location)
            }

            locationRepository.onRemoval { locationName, _ ->
                if (!locationCache.contains(locationName)) return@onRemoval
                locationCache.delete(locationName)
            }
        }
    }

    override suspend fun getLocation(name: String): WorldLocation? {
        return this.locationCache.get(name) ?: this.locationRepository.get(name).await()
    }

    override suspend fun createLocation(location: WorldLocation) {
        newSuspendedTransaction(Dispatchers.IO) {
            WorldLocationEntity.new {
                this.locationName = location.name
                this.worldName = location.worldName
                this.x = location.x
                this.y = location.y
                this.z = location.z
                this.yaw = location.yaw
                this.pitch = location.pitch
                this.blockData = location.blockData?.asString
            }
        }

        this.locationRepository.insert(location.name, location)
    }

    override suspend fun deleteLocation(name: String) {
        newSuspendedTransaction(Dispatchers.IO) {
            WorldLocationEntity.find { WorldLocationTable.locationName eq name }.singleOrNull()?.delete()
            unloadLocation(name)
        }
    }

    override suspend fun loadLocations(): Deferred<List<WorldLocation>> = withContext(Dispatchers.IO) {
        suspendedTransactionAsync {
            val locations = mutableListOf<WorldLocation>()

            for (locationEntity in WorldLocationEntity.all()) {
                val location = WorldLocation(
                    locationEntity.locationName,
                    locationEntity.worldName,
                    locationEntity.x,
                    locationEntity.y,
                    locationEntity.z,
                    locationEntity.yaw,
                    locationEntity.pitch,
                    if (locationEntity.blockData == null) null else Bukkit.createBlockData(locationEntity.blockData!!)
                )

                if (!locationRepository.contains(location.name).await())
                    locationRepository.insert(location.name, location)

                locations.add(location)
            }

            locations
        }
    }

    override suspend fun unloadLocation(name: String) {
        this.locationRepository.delete(name)
    }

}