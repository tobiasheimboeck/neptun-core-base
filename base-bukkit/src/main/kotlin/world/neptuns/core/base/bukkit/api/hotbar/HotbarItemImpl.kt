package world.neptuns.core.base.bukkit.api.hotbar

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import world.neptuns.base.bukkit.api.hotbar.HotbarItem

class HotbarItemImpl(
    override val slot: Int,
    override val permission: String?,
    override val itemStack: ItemStack,
    override var action: (PlayerInteractEvent) -> Unit
) : HotbarItem {

    override fun addToPlayer(player: Player) {
        if (this.permission == null || !player.hasPermission(this.permission)) return
        player.inventory.setItem(this.slot, this.itemStack)
    }

}