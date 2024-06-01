package world.neptuns.core.base.api.currency

import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import java.io.Serializable

abstract class Currency(val name: String, val giftable: Boolean, val earnMethods: List<EarnMethod>) : Serializable {

    abstract fun onEarn(offlinePlayer: NeptunOfflinePlayer): Boolean
    abstract fun onGift(senderOfflinePlayer: NeptunOfflinePlayer, receiverOfflinePlayer: NeptunOfflinePlayer): Boolean
    abstract fun onSpend(offlinePlayer: NeptunOfflinePlayer): Boolean

    enum class EarnMethod {
        MINIGAME,
        LOBBY_GAME,
        DAILY_REWARD
    }

}