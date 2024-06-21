package world.neptuns.core.base.velocity.command.impl

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.command.NeptunCommand
import world.neptuns.core.base.api.command.NeptunCommandPlatform
import world.neptuns.core.base.api.command.NeptunCommandSender
import world.neptuns.core.base.api.command.NeptunMainCommandExecutor
import world.neptuns.core.base.api.command.subcommand.NeptunSubCommandExecutor
import world.neptuns.core.base.api.language.LanguageController
import world.neptuns.core.base.api.language.color.LanguageColorService
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.language.properties.LanguagePropertiesService
import world.neptuns.core.base.common.packet.LanguagePropertiesChangePacket
import world.neptuns.streamline.api.NeptunStreamlineProvider

@NeptunCommand(platform = NeptunCommandPlatform.VELOCITY, "language", "core.language", ["lang"])
class LanguageCommand(
    private val languageColorService: LanguageColorService,
    private val languagePropertiesService: LanguagePropertiesService,
    private val languageController: LanguageController,
) : NeptunMainCommandExecutor() {

    override suspend fun defaultExecute(sender: NeptunCommandSender) {
        val player = sender.castTo(Player::class.java) ?: return

        val playerAdapter = NeptunCoreProvider.api.getPlayerAdapter()

        val properties = this.languagePropertiesService.getProperties(player.uniqueId) ?: return

//        val requestedLanguage = this.languageController.getLanguage(LangKey.fromString(args[0]))
//
//        if (requestedLanguage == null) {
//            player.sendMessage(Component.text("Language ${args[0]} not found!"))
//            return
//        }

//        properties.langKey = requestedLanguage.key
        this.languagePropertiesService.bulkUpdateEntry(LanguageProperties.Update.LANGUAGE_KEY, player.uniqueId, properties.langKey.asString(), true)
        NeptunStreamlineProvider.api.packetController.sendPacket(LanguagePropertiesChangePacket(player.uniqueId, properties))

        playerAdapter.sendMessage(player, "core.base.language.key_change", Placeholder.parsed("name", properties.langKey.asString()))
    }

    override suspend fun onDefaultTabComplete(sender: NeptunCommandSender, args: List<String>): List<String> = emptyList()

    override fun initSubCommands(subCommandExecutors: MutableList<NeptunSubCommandExecutor>) {

    }

}