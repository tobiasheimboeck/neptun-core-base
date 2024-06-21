package world.neptuns.base.bukkit.api.hotbar.builder

import world.neptuns.base.bukkit.api.hotbar.Hotbar
import world.neptuns.core.base.api.util.Builder

interface HotbarBuilder : Builder<Hotbar> {

    fun key(key: String): HotbarBuilder

    fun newPage(pageBuilder: HotbarPageBuilder): HotbarBuilder

}