package world.neptuns.core.base.common.repository.player

import world.neptuns.core.base.api.player.NeptunOnlinePlayer
import world.neptuns.streamline.api.cache.MapCache
import java.util.*

class OnlinePlayerCache : MapCache<UUID, NeptunOnlinePlayer>()