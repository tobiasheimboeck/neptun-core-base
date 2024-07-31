package world.neptuns.core.base.bukkit.api.metadata

import org.bukkit.entity.Entity
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import world.neptuns.base.bukkit.api.metadata.MetadataController

class MetadataControllerImpl(private val plugin: JavaPlugin) : MetadataController {

    override fun apply(entities: List<Entity>, key: String, value: Any) {
        entities.forEach { apply(it, key, value) }
    }

    override fun apply(entity: Entity, key: String, value: Any) {
        if (has(entity, key)) remove(entity, key)
        entity.setMetadata(key, FixedMetadataValue(this.plugin, value))
    }

    override fun remove(entities: List<Entity>, key: String) {
        entities.forEach { remove(it, key) }
    }

    override fun remove(entity: Entity, key: String) {
        entity.removeMetadata(key, this.plugin)
    }

}