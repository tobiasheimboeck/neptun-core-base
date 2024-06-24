package world.neptuns.core.base.common.database.player

import org.jetbrains.exposed.dao.id.IntIdTable

object OfflinePlayerTable : IntIdTable("offline_players") {
    val uuid = OfflinePlayerTable.uuid("uuid").uniqueIndex()
    val name = OfflinePlayerTable.varchar("name", 255)
    val firstLoginTimestamp = OfflinePlayerTable.long("first_login_timestamp")
    val lastLoginTimestamp = OfflinePlayerTable.long("last_login_timestamp")
    val lastLogoutTimestamp = OfflinePlayerTable.long("last_logout_timestamp")
    val onlineTime = OfflinePlayerTable.long("online_time")
    val crystals = OfflinePlayerTable.long("crystals")
    val shards = OfflinePlayerTable.long("shards")
    val skinValue = OfflinePlayerTable.text("skin_value")
    val skinSignature = OfflinePlayerTable.text("skin_signature")
}