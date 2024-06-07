package world.neptuns.base.bukkit.api.hotbar

import org.bukkit.entity.Player

interface Hotbar {

    val key: String
    val pages: List<HotbarPage>
    var activePage: HotbarPage?

    fun getItem(player: Player, slot: Int): HotbarItem?
    fun addPage(page: HotbarPage)

    fun setActivePage(player: Player, pageId: Int)

    fun toIndexPage(player: Player)
    fun toNextPage(player: Player)
    fun toPreviousPage(player: Player)

    // sets the page content to the player
    fun navigateTo(player: Player)

}