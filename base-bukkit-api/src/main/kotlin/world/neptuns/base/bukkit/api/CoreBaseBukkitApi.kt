package world.neptuns.base.bukkit.api

import org.bukkit.entity.Player
import world.neptuns.base.bukkit.api.bossbar.BossbarController
import world.neptuns.base.bukkit.api.hotbar.HotbarController
import world.neptuns.base.bukkit.api.item.ItemDataController
import world.neptuns.base.bukkit.api.metadata.MetadataController
import world.neptuns.base.bukkit.api.metadata.PersistentDataController
import world.neptuns.base.bukkit.api.scoreboard.SidebarBuilder
import world.neptuns.base.bukkit.api.scoreboard.SidebarController

interface CoreBaseBukkitApi {

    val bossbarController: BossbarController

    val itemDataController: ItemDataController
    val metadataController: MetadataController
    val persistentDataController: PersistentDataController

    val hotbarController: HotbarController

    val sidebarController: SidebarController

    fun newSidebar(viewer: Player): SidebarBuilder

}