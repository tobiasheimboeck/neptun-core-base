package world.neptuns.base.bukkit.api.hotbar.builder

import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.base.bukkit.api.hotbar.HotbarPage
import world.neptuns.core.base.api.util.Builder

interface HotbarPageBuilder : Builder<HotbarPage> {

    fun id(id: Int): HotbarPageBuilder
    fun item(itemBuilder: HotbarItemBuilder): HotbarPageBuilder

    companion object {
        fun builder() = NeptunCoreBukkitProvider.api.hotbarController.newHotbarPage()
    }

}