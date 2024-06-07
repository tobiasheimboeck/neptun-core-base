package world.neptuns.base.bukkit.api.hotbar

import org.bukkit.entity.Player
import world.neptuns.base.bukkit.api.NeptunBukkitPlugin
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarBuilder
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarItemBuilder
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarPageBuilder

interface HotbarController {

    val hotbars: MutableMap<String, Hotbar>

    fun getHotbar(key: String): Hotbar?

    fun setHotbarToPlayer(player: Player, key: String)

    fun setFallbackHotbarToPlayer(player: Player, plugin: NeptunBukkitPlugin) {
        val fallbackHotbarKey = plugin.fallbackHotbarKey ?: throw NullPointerException("No default hotbar found...")
        setHotbarToPlayer(player, fallbackHotbarKey)
    }

    fun getCurrentPageId(player: Player): Int?
    fun setCurrentPageId(player: Player, page: Int)
    fun removeCurrentPageId(player: Player)

    fun newHotbar(): HotbarBuilder
    fun newHotbarPage(): HotbarPageBuilder
    fun newHotbarItem(): HotbarItemBuilder

}