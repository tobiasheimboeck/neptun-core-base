package world.neptuns.core.base.velocity.command.impl

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandExecutor
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.color.LanguageColorController
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.language.properties.LanguagePropertiesController
import world.neptuns.core.base.common.packet.LanguagePropertiesChangePacket
import world.neptuns.streamline.api.NeptunStreamlineProvider

@NeptunCommand(type = NeptunCommandPlatform.VELOCITY, "language", "core.language", arrayOf("lang"))
class LanguageCommand(
    private val languageColorController: LanguageColorController,
    private val languagePropertiesController: LanguagePropertiesController,
    private val languageController: LanguageController,
) : NeptunCommandExecutor {

    override suspend fun execute(sender: NeptunCommandSender, args: List<String>) {
        if (!sender.isPlayer()) return

        val player = sender.castTo(Player::class.java)
        val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter(Player::class.java)

        val properties = this.languagePropertiesController.getProperties(player.uniqueId) ?: return

        val requestedLanguage = this.languageController.getLanguage(LangKey.fromString(args[0]))

        if (requestedLanguage == null) {
            player.sendMessage(Component.text("Language ${args[0]} not found!"))
            return
        }

        properties.langKey = requestedLanguage.key
        this.languagePropertiesController.bulkUpdateEntry(LanguageProperties.Update.LANGUAGE_KEY, player.uniqueId, properties.langKey.asString(), true)
        NeptunStreamlineProvider.api.packetController.sendPacket(LanguagePropertiesChangePacket(player.uniqueId, properties))

        playerAdapter.sendMessage(player, "core.base.language.key_change", Placeholder.parsed("name", properties.langKey.asString()))
    }

    override fun sendUsage(sender: NeptunCommandSender) {
        // /language <name>
    }

    override suspend fun onTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> {
        return emptyList()
    }

}