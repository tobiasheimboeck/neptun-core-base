package world.neptuns.core.base.bukkit.database.location

import org.jetbrains.exposed.dao.id.IntIdTable

object WorldLocationTable : IntIdTable("world_locations") {
    val locationName = varchar("location_name", 50).uniqueIndex()
    val worldName = varchar("world_name", 50)
    val x = double("x")
    val y = double("y")
    val z = double("z")
    val yaw = float("yaw")
    val pitch = float("pitch")
    val blockData = text("block_data").nullable()
}