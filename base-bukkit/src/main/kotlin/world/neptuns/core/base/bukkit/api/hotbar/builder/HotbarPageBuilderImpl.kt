package world.neptuns.core.base.bukkit.api.hotbar.builder

import world.neptuns.base.bukkit.api.hotbar.HotbarItem
import world.neptuns.base.bukkit.api.hotbar.HotbarPage
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarItemBuilder
import world.neptuns.base.bukkit.api.hotbar.builder.HotbarPageBuilder
import world.neptuns.core.base.bukkit.api.hotbar.HotbarPageImpl

class HotbarPageBuilderImpl : HotbarPageBuilder {

    private var id: Int = 0
    private var items = mutableListOf<HotbarItem>()

    override fun id(id: Int): HotbarPageBuilder {
        this.id = id
        return this
    }

    override fun item(itemBuilder: HotbarItemBuilder): HotbarPageBuilder {
        this.items.add(itemBuilder.build())
        return this
    }

    override fun build(): HotbarPage {
        return HotbarPageImpl(this.id, this.items)
    }

}