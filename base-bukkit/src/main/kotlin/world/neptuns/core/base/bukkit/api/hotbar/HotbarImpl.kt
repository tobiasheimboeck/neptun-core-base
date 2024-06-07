package world.neptuns.core.base.bukkit.api.hotbar

import org.bukkit.entity.Player
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.base.bukkit.api.hotbar.Hotbar
import world.neptuns.base.bukkit.api.hotbar.HotbarItem
import world.neptuns.base.bukkit.api.hotbar.HotbarPage

class HotbarImpl(
    override val key: String,
    override val pages: MutableList<HotbarPage>,
) : Hotbar {

    override var activePage: HotbarPage? = null

    init {
        NeptunCoreBukkitProvider.api.hotbarController.hotbars[this.key] = this
    }

    override fun getItem(player: Player, slot: Int): HotbarItem? {
        return this.activePage?.items?.find { it.slot == slot }
    }

    override fun addPage(page: HotbarPage) {
        if (this.pages.any { it.id == page.id }) return
        this.pages.add(page)
    }

    override fun setActivePage(player: Player, pageId: Int) {
        NeptunCoreBukkitProvider.api.hotbarController.removeCurrentPageId(player)
        NeptunCoreBukkitProvider.api.hotbarController.setCurrentPageId(player, pageId)

        val currentPage = this.pages[pageId]
        currentPage.clearCurrentInventory(player)
        currentPage.addToPlayer(player)
    }

    override fun toIndexPage(player: Player) {
        setActivePage(player, 0)
    }

    override fun toNextPage(player: Player) {
        changePage(player, PageChangeOption.NEXT)
    }

    override fun toPreviousPage(player: Player) {
        changePage(player, PageChangeOption.PREVIOUS)
    }

    override fun navigateTo(player: Player) {
        toIndexPage(player)
    }

    private fun changePage(player: Player, option: PageChangeOption) {
        val page = this.activePage ?: return
        val newPageId = if (option == PageChangeOption.NEXT) page.id.inc() else page.id.dec()
        setActivePage(player, newPageId)
    }

    private enum class PageChangeOption {
        NEXT,
        PREVIOUS
    }

}