package world.neptuns.core.base.api.currency.type

import world.neptuns.core.base.api.currency.Currency
import world.neptuns.core.base.api.player.NeptunOfflinePlayer

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