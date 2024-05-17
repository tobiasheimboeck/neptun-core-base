package world.neptuns.core.base.common.repository.color

import org.redisson.api.RedissonClient
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.repository.impl.RMultimapRepository
import java.util.*

class LanguageColorRepository(redissonClient: RedissonClient) : RMultimapRepository<UUID, LanguageColor>(redissonClient, "language_colors")