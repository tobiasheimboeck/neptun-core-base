package world.neptuns.core.base.api.player

interface PlayerSession {

    val firstLoginTimestamp: Long
    var lastLoginTimestamp: Long
    var lastLogoutTimestamp: Long

    var onlineTime: Long

    fun updateOnlineTime() {
        val playedDuration: Long = this.lastLogoutTimestamp - this.lastLoginTimestamp
        this.onlineTime += playedDuration
    }

}