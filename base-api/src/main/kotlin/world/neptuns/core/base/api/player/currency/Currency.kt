package world.neptuns.core.base.api.player.currency

import world.neptuns.core.base.api.player.NeptunOfflinePlayer

abstract class Currency(val name: String, val giftable: Boolean, val earnMethods: List<EarnMethod>) {

    abstract fun onEarn(offlinePlayer: NeptunOfflinePlayer)
    abstract fun onGift(senderOfflinePlayer: NeptunOfflinePlayer, receiverOfflinePlayer: NeptunOfflinePlayer)
    abstract fun onSpend(offlinePlayer: NeptunOfflinePlayer)

    enum class EarnMethod {
        MINIGAME,
        LOBBY_GAME,
        DAILY_REWARD
    }

}