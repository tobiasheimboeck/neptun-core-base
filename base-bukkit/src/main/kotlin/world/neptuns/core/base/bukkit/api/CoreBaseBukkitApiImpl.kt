package world.neptuns.core.base.bukkit.api

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import world.neptuns.base.bukkit.api.CoreBaseBukkitApi
import world.neptuns.base.bukkit.api.bossbar.BossbarController
import world.neptuns.base.bukkit.api.hotbar.HotbarController
import world.neptuns.base.bukkit.api.item.ItemBuilder
import world.neptuns.base.bukkit.api.item.ItemDataController
import world.neptuns.base.bukkit.api.metadata.MetadataController
import world.neptuns.base.bukkit.api.plugin.NeptunBukkitPlugin
import world.neptuns.base.bukkit.api.scoreboard.SidebarBuilder
import world.neptuns.base.bukkit.api.scoreboard.SidebarController
import world.neptuns.core.base.bukkit.api.bossbar.BossbarControllerImpl
import world.neptuns.core.base.bukkit.api.hotbar.HotbarControllerImpl
import world.neptuns.core.base.bukkit.api.item.ItemBuilderImpl
import world.neptuns.core.base.bukkit.api.item.ItemDataControllerImpl
import world.neptuns.core.base.bukkit.api.metadata.MetadataControllerImpl
import world.neptuns.core.base.bukkit.api.scoreboard.SidebarBuilderImpl
import world.neptuns.core.base.bukkit.api.scoreboard.SidebarControllerImpl

class CoreBaseBukkitApiImpl(private val plugin: JavaPlugin) : CoreBaseBukkitApi {

    override val bossbarController: BossbarController = BossbarControllerImpl()

    override val itemDataController: ItemDataController = ItemDataControllerImpl(this.plugin)
    override val metadataController: MetadataController = MetadataControllerImpl(this.plugin)

    override val hotbarController: HotbarController = HotbarControllerImpl()
    override val sidebarController: SidebarController = SidebarControllerImpl()

    override fun newItem(type: Material): ItemBuilder = ItemBuilderImpl(type)
    override fun newSidebar(viewer: Player): SidebarBuilder = SidebarBuilderImpl(viewer)


}