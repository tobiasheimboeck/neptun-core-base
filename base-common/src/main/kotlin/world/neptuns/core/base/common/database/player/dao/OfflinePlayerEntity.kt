package world.neptuns.core.base.common.database.player.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import world.neptuns.core.base.common.database.player.OfflinePlayerTable

class OfflinePlayerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OfflinePlayerEntity>(OfflinePlayerTable)

    var uuid by OfflinePlayerTable.uuid
    var name by OfflinePlayerTable.name
    var firstLoginTimestamp by OfflinePlayerTable.firstLoginTimestamp
    var lastLoginTimestamp by OfflinePlayerTable.lastLoginTimestamp
    var lastLogoutTimestamp by OfflinePlayerTable.lastLogoutTimestamp
    var onlineTime by OfflinePlayerTable.onlineTime
    var crystals by OfflinePlayerTable.crystals
    var shards by OfflinePlayerTable.shards
    var skinValue by OfflinePlayerTable.skinValue
    var skinSignature by OfflinePlayerTable.skinSignature
}