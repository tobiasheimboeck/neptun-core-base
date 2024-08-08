package world.neptuns.base.bukkit.api.location

import kotlinx.coroutines.Deferred

interface WorldLocationService {

    suspend fun getLocation(name: String): WorldLocation?

    suspend fun createLocation(location: WorldLocation)
    suspend fun deleteLocation(name: String)

    suspend fun loadLocations(): Deferred<List<WorldLocation>>
    suspend fun unloadLocation(name: String)

}