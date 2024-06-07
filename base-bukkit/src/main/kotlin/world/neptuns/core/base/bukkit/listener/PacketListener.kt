package world.neptuns.core.base.bukkit.listener

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import world.neptuns.core.base.common.packet.PlayerPerformCommandPacket
import world.neptuns.core.base.common.packet.PlayerTeleportPacket
import world.neptuns.core.base.common.packet.PlayerTeleportToPlayerPacket
import world.neptuns.streamline.api.NeptunStreamlineProvider
import world.neptuns.streamline.api.packet.NetworkChannelRegistry

class PacketListener(private val plugin: JavaPlugin) {

    private val packetController = NeptunStreamlineProvider.api.packetController

    suspend fun listen() {
        this.packetController.listenForPacket(NetworkChannelRegistry.SERVICE, PlayerTeleportPacket::class.java) { packet ->
            val player = Bukkit.getPlayer(packet.uuid) ?: return@listenForPacket
            val worldName = packet.worldName ?: player.world.name
            val world = Bukkit.getWorld(worldName) ?: return@listenForPacket

            player.teleportAsync(Location(world, packet.x, packet.y, packet.z, packet.yaw, packet.pitch))
        }

        this.packetController.listenForPacket(NetworkChannelRegistry.SERVICE, PlayerTeleportToPlayerPacket::class.java) { packet ->
            val player = Bukkit.getPlayer(packet.uuid) ?: return@listenForPacket
            val target = Bukkit.getPlayer(packet.targetUniqueId) ?: return@listenForPacket

            player.teleportAsync(target.location)
        }

        this.packetController.listenForPacket(NetworkChannelRegistry.SERVICE, PlayerPerformCommandPacket::class.java) { packet ->
            val player = Bukkit.getPlayer(packet.uuid) ?: return@listenForPacket

            Bukkit.getScheduler().runTask(this.plugin, Runnable {
                player.performCommand(packet.command)
            })
        }
    }

}