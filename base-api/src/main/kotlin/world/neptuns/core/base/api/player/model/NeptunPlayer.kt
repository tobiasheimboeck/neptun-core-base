package world.neptuns.core.base.api.player.model

interface NeptunPlayer : NeptunOfflinePlayer {

    var nickName: String?

    var currentProxyName: String
    var currentServiceName: String

}