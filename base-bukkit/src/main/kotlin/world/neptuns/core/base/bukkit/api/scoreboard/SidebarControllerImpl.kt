package world.neptuns.core.base.bukkit.api.scoreboard

import world.neptuns.base.bukkit.api.scoreboard.Sidebar
import world.neptuns.base.bukkit.api.scoreboard.SidebarController
import java.util.*

class SidebarControllerImpl : SidebarController {

    private val sidebars = mutableSetOf<Sidebar>()

    override fun getSidebar(uuid: UUID): Sidebar? = this.sidebars.find { it.viewer.uniqueId == uuid }

    override fun registerSidebar(sidebar: Sidebar) {
        this.sidebars.add(sidebar)
    }

    override fun unregisterSidebar(uuid: UUID) {
        val sidebar: Sidebar = getSidebar(uuid) ?: return
        sidebar.reset()
        this.sidebars.remove(sidebar)
    }

}