package world.neptuns.core.base.api.player.currency

import world.neptuns.core.base.api.player.NeptunOfflinePlayer

class CurrencyRegistry {

    class Crystals : Currency("Crystals", false, listOf(EarnMethod.MINIGAME, EarnMethod.LOBBY_GAME, EarnMethod.DAILY_REWARD)) {
        override fun onEarn(offlinePlayer: NeptunOfflinePlayer) {}
        override fun onGift(senderOfflinePlayer: NeptunOfflinePlayer, receiverOfflinePlayer: NeptunOfflinePlayer) {}
        override fun onSpend(offlinePlayer: NeptunOfflinePlayer) {}
    }

    class Shards : Currency("Shards", false, listOf(EarnMethod.MINIGAME, EarnMethod.DAILY_REWARD)) {
        override fun onEarn(offlinePlayer: NeptunOfflinePlayer) {}
        override fun onGift(senderOfflinePlayer: NeptunOfflinePlayer, receiverOfflinePlayer: NeptunOfflinePlayer) {}
        override fun onSpend(offlinePlayer: NeptunOfflinePlayer) {}
    }

}