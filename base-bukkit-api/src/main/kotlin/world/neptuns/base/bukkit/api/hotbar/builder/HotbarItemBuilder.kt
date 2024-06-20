package world.neptuns.base.bukkit.api.hotbar.builder

import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.base.bukkit.api.hotbar.HotbarItem
import world.neptuns.core.base.api.util.Builder

interface HotbarItemBuilder : Builder<HotbarItem> {

    fun slot(slot: Int): HotbarItemBuilder

    fun permission(permission: String): HotbarItemBuilder

    fun item(itemStack: ItemStack): HotbarItemBuilder

    fun interact(action: (PlayerInteractEvent) -> Unit): HotbarItemBuilder

    companion object {
        fun builder() = NeptunCoreBukkitProvider.api.hotbarController.newHotbarItem()
    }

}