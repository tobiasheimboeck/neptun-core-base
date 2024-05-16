package world.neptuns.core.base.common.repository

import org.redisson.api.RedissonClient
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.core.base.api.repository.impl.RMapRepository
import java.util.*

class OnlinePlayerRepository(redissonClient: RedissonClient) : RMapRepository<UUID, NeptunOnlinePlayer>(redissonClient, "online_players")