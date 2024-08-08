package world.neptuns.core.base.bukkit.database.location.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import world.neptuns.core.base.bukkit.database.location.WorldLocationTable

class WorldLocationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WorldLocationEntity>(WorldLocationTable)

    var locationName by WorldLocationTable.locationName
    var worldName by WorldLocationTable.worldName
    var x by WorldLocationTable.x
    var y by WorldLocationTable.y
    var z by WorldLocationTable.z
    var yaw by WorldLocationTable.yaw
    var pitch by WorldLocationTable.pitch
    var blockData by WorldLocationTable.blockData
}