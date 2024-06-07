package world.neptuns.core.base.bukkit.api.hotbar.builder

import world.neptuns.base.bukkit.api.hotbar.Hotbar
import world.neptuns.base.bukkit.api.hotbar.HotbarPage
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarBuilder
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarPageBuilder
import world.neptuns.core.base.bukkit.api.hotbar.HotbarImpl

class HotbarBuilderImpl : HotbarBuilder {

    private var key: String? = null
    private val pages = mutableListOf<HotbarPage>()

    override fun key(key: String): HotbarBuilder {
        this.key = key
        return this
    }

    override fun newPage(pageBuilder: HotbarPageBuilder): HotbarBuilder {
        this.pages.add(pageBuilder.build())
        return this
    }

    override fun build(): Hotbar {
        return HotbarImpl(this.key!!, this.pages)
    }

}