package world.neptuns.core.base.common.api.player

import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.skin.SkinProfile
import world.neptuns.core.base.common.api.skin.SkinProfileImpl
import java.util.*

class NeptunOfflinePlayerImpl(
    override val uuid: UUID,
    override var name: String,
    override val firstLoginTimestamp: Long,
    override var lastLoginTimestamp: Long,
    override var lastLogoutTimestamp: Long,
    override var onlineTime: Long,
    override val skinProfile: SkinProfile,
    override var crystals: Long,
    override var shards: Long,
) : NeptunOfflinePlayer {

    companion object {
        fun create(uuid: UUID, name: String, skinValue: String, skinSignature: String) = NeptunOfflinePlayerImpl(
            uuid,
            name,
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            0L,
            0L,
            SkinProfileImpl(skinValue, skinSignature),
            0L,
            1L
        )
    }

}