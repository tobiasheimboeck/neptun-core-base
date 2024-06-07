package world.neptuns.base.bukkit.api.scoreboard

import java.util.*

interface SidebarController {

    fun getSidebar(uuid: UUID): Sidebar?

    fun registerSidebar(sidebar: Sidebar)
    fun unregisterSidebar(uuid: UUID)

}