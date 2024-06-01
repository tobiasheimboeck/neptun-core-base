package world.neptuns.core.base.common.repository.player

import org.jetbrains.exposed.sql.Table

object OfflinePlayerTable : Table("offline_players") {
    val uuid = uuid("uuid")
    val name = varchar("name", 255)
    val firstLoginTimestamp = long("first_login_timestamp")
    val lastLoginTimestamp = long("last_login_timestamp")
    val lastLogoutTimestamp = long("last_logout_timestamp")
    val onlineTime = long("online_time")
    val crystals = long("crystals")
    var shards = long("shards")
    val skinValue = text("skin_value")
    val skinSignature = text("skin_signature")

    override val primaryKey: PrimaryKey = PrimaryKey(this.uuid)
}