package world.neptuns.base.bukkit.api.location

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import java.io.Serializable

data class WorldLocation(
    val name: String,
    val worldName: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
    val blockData: BlockData? = null
) : Serializable {

    fun toBukkitLocation() = Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z, this.yaw, this.pitch)

    companion object {

        fun fromBukkitLocation(name: String, location: Location, blockData: BlockData? = null) = WorldLocation(
            name,
            location.world.name,
            location.x,
            location.y,
            location.z,
            location.yaw,
            location.pitch,
            blockData
        )

    }

}