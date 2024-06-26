package world.neptuns.core.base.common.repository.player

import org.redisson.api.RedissonClient
import world.neptuns.streamline.api.repository.RListRepository

class OnlinePlayerNameRepository(redissonClient: RedissonClient) : RListRepository<String>(redissonClient, "online_player_names")