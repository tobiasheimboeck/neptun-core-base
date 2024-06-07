package world.neptuns.core.base.bukkit.api.hotbar

import org.bukkit.entity.Player
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.base.bukkit.api.hotbar.Hotbar
import world.neptuns.base.bukkit.api.hotbar.HotbarController
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarBuilder
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarItemBuilder
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarPageBuilder
import world.neptuns.core.base.bukkit.api.hotbar.builder.HotbarBuilderImpl
import world.neptuns.core.base.bukkit.api.hotbar.builder.HotbarItemBuilderImpl
import world.neptuns.core.base.bukkit.api.hotbar.builder.HotbarPageBuilderImpl

class HotbarControllerImpl : HotbarController {

    override val hotbars: MutableMap<String, Hotbar> = mutableMapOf()

    override fun getHotbar(key: String): Hotbar? {
        return this.hotbars[key]
    }

    override fun setHotbarToPlayer(player: Player, key: String) {
        val hotbar = getHotbar(key) ?: return
        hotbar.navigateTo(player)
    }

    override fun getCurrentPageId(player: Player): Int? {
        return NeptunCoreBukkitProvider.api.metadataController.get<Int>(player, "active_hotbar_page")
    }

    override fun setCurrentPageId(player: Player, page: Int) {
        NeptunCoreBukkitProvider.api.metadataController.apply(player, "active_hotbar_page", page)
    }

    override fun removeCurrentPageId(player: Player) {
        NeptunCoreBukkitProvider.api.metadataController.remove(player, "active_hotbar_page")
    }

    override fun newHotbar(): HotbarBuilder {
        return HotbarBuilderImpl()
    }

    override fun newHotbarPage(): HotbarPageBuilder {
        return HotbarPageBuilderImpl()
    }

    override fun newHotbarItem(): HotbarItemBuilder {
        return HotbarItemBuilderImpl()
    }

}