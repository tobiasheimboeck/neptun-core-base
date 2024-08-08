package world.neptuns.core.base.bukkit.repository.location

import org.redisson.api.RedissonClient
import world.neptuns.base.bukkit.api.location.WorldLocation
import world.neptuns.streamline.api.cache.MapCache
import world.neptuns.streamline.api.repository.RMapRepository

class WorldLocationRepository(redissonClient: RedissonClient) : RMapRepository<String, WorldLocation>(redissonClient, "world_locations")
class WorldLocationCache : MapCache<String, WorldLocation>()