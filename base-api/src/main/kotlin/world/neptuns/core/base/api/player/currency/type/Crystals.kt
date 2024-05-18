package world.neptuns.core.base.api.player.currency.type

import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.api.player.currency.Currency

class Crystals : Currency("Crystals", false, listOf(EarnMethod.MINIGAME, EarnMethod.LOBBY_GAME, EarnMethod.DAILY_REWARD)) {

    override fun onEarn(offlinePlayer: NeptunOfflinePlayer): Boolean {
        return false
    }

    override fun onGift(senderOfflinePlayer: NeptunOfflinePlayer, receiverOfflinePlayer: NeptunOfflinePlayer): Boolean {
        return false
    }

    override fun onSpend(offlinePlayer: NeptunOfflinePlayer): Boolean {
        return false
    }

}