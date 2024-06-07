package world.neptuns.core.base.bukkit.api.metadata

import org.bukkit.entity.LivingEntity
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import world.neptuns.base.bukkit.api.metadata.MetadataController

class MetadataControllerImpl(private val plugin: JavaPlugin) : MetadataController {

    override fun apply(entity: LivingEntity, key: String, value: Any) {
        if (has(entity, key)) remove(entity, key)
        entity.setMetadata(key, FixedMetadataValue(this.plugin, value))
    }

    override fun remove(entity: LivingEntity, key: String) {
        entity.removeMetadata(key, this.plugin)
    }

}