package world.neptuns.base.bukkit.api.hotbar

import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

interface HotbarItem {

    val slot: Int
    val permission: String?
    val itemStack: ItemStack
    var action: (PlayerInteractEvent) -> (Unit)

    fun addToPlayer(player: Player)

}