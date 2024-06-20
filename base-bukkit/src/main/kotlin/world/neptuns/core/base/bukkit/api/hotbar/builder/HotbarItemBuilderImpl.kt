package world.neptuns.core.base.bukkit.api.hotbar.builder

import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import world.neptuns.base.bukkit.api.hotbar.HotbarItem
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarItemBuilder
import world.neptuns.core.base.bukkit.api.hotbar.HotbarItemImpl

class HotbarItemBuilderImpl : HotbarItemBuilder {

    private var slot: Int = 0
    private var permission: String? = null
    private var itemStack: ItemStack = ItemStack(Material.AIR)
    private var action: (PlayerInteractEvent) -> (Unit) = {}

    override fun slot(slot: Int): HotbarItemBuilder {
        this.slot = slot
        return this
    }

    override fun permission(permission: String): HotbarItemBuilder {
        this.permission = permission
        return this
    }

    override fun item(itemBuilder: ItemBuilder): HotbarItemBuilder {
        this.itemStack = itemBuilder.build()
        return this
    }

    override fun interact(action: (PlayerInteractEvent) -> Unit): HotbarItemBuilder {
        this.action = action
        return this
    }

    override fun build(): HotbarItem {
        return HotbarItemImpl(
            this.slot,
            this.permission,
            this.itemStack,
            this.action
        )
    }

}