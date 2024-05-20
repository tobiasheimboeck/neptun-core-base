package world.neptuns.core.base.common.repository.player

import world.neptuns.core.base.api.cache.impl.MapCache
import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import java.util.*

class OnlinePlayerCache : MapCache<UUID, NeptunOnlinePlayer>()