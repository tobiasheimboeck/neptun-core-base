package world.neptuns.core.base.common.repository.language

import org.redisson.api.RedissonClient
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.repository.impl.RMapRepository
import java.util.*

class LanguagePropertiesRepository(redissonClient: RedissonClient) : RMapRepository<UUID, LanguageProperties>(redissonClient, "language_properties")