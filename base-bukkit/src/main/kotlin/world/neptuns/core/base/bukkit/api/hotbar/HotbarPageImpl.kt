package world.neptuns.core.base.bukkit.api.hotbar

import org.bukkit.entity.Player
import world.neptuns.base.bukkit.api.hotbar.HotbarItem
import world.neptuns.base.bukkit.api.hotbar.HotbarPage

class HotbarPageImpl(
    override var id: Int,
    override val items: List<HotbarItem>
) : HotbarPage {

    override fun clearCurrentInventory(player: Player) {
        player.inventory.clear()
    }

    override fun addToPlayer(player: Player) {
        this.items.forEach { it.addToPlayer(player) }
    }

}