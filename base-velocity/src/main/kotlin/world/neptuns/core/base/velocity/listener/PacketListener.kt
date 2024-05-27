package world.neptuns.core.base.velocity.listener

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.common.packet.MessageToPlayerPacket
import world.neptuns.core.base.common.packet.PlayerConnectToServicePacket
import world.neptuns.streamline.api.NeptunStreamlineProvider
import world.neptuns.streamline.api.packet.NetworkChannelRegistry

internal class PacketListener(private val proxyServer: ProxyServer) {

    private val packetController = NeptunStreamlineProvider.api.packetController

    suspend fun listen() {
        NeptunCoreProvider.api.playerController.listenToUpdatePackets()
        NeptunCoreProvider.api.languagePropertiesController.listenToUpdatePackets()

        this.packetController.listenForPacket(NetworkChannelRegistry.PROXY, MessageToPlayerPacket::class.java) { packet ->
            val player = this.proxyServer.getPlayer(packet.uuid).orElse(null)
            val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter(Player::class.java)

            val placeholders = mutableListOf<TagResolver>()

            for (placeholderAsString in packet.toReplace) {
                val tagResolver = Placeholder.parsed(placeholderAsString.first, placeholderAsString.second)
                placeholders.add(tagResolver)
            }

            playerAdapter.sendMessage(player, LineKey.key(packet.key), *placeholders.toTypedArray())
        }

        this.packetController.listenForPacket(NetworkChannelRegistry.PROXY, PlayerConnectToServicePacket::class.java) { packet ->
            val player = this.proxyServer.getPlayer(packet.uuid).orElse(null)
            val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter(Player::class.java)

            if (packet.isLobbyRequest) {
                playerAdapter.transferPlayerToLobby(player.uniqueId)
                return@listenForPacket
            }

            val serviceName = packet.serviceName ?: return@listenForPacket
            playerAdapter.transferPlayerToService(packet.uuid, serviceName)
        }
    }

}