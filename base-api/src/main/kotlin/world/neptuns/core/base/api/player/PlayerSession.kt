package world.neptuns.core.base.api.player

interface PlayerSession {

    val firstLoginTimestamp: Long
    var lastLoginTimestamp: Long
    var lastLogoutTimestamp: Long

    var onlineTime: Long


}