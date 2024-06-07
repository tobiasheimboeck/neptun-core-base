package world.neptuns.base.bukkit.api.hotbar.builder

import org.bukkit.event.player.PlayerInteractEvent
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.base.bukkit.api.hotbar.HotbarItem
import world.neptuns.base.bukkit.api.item.ItemBuilder
import world.neptuns.core.base.api.utils.Builder

interface HotbarItemBuilder : Builder<HotbarItem> {

    fun slot(slot: Int): HotbarItemBuilder

    fun permission(permission: String): HotbarItemBuilder

    fun item(itemBuilder: ItemBuilder): HotbarItemBuilder

    fun interact(action: (PlayerInteractEvent) -> Unit): HotbarItemBuilder

    companion object {
        fun builder() = NeptunCoreBukkitProvider.api.hotbarController.newHotbarItem()
    }

}