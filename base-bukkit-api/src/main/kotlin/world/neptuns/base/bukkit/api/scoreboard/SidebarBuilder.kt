package world.neptuns.base.bukkit.api.scoreboard

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider

interface SidebarBuilder {

    fun setTitle(title: Component): SidebarBuilder

    fun addLine(line: Component): SidebarBuilder

    fun addBlankLine(): SidebarBuilder

    fun build(): Sidebar

    companion object {
        fun builder(viewer: Player) = NeptunCoreBukkitProvider.api.newSidebar(viewer)
    }

}