package world.neptuns.base.bukkit.api.hotbar

import org.bukkit.entity.Player

interface HotbarPage {

    var id: Int
    val items: List<HotbarItem>

    fun clearCurrentInventory(player: Player)
    fun addToPlayer(player: Player)

}