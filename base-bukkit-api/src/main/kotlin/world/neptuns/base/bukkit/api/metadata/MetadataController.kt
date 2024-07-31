package world.neptuns.base.bukkit.api.metadata

import org.bukkit.entity.Entity

@Suppress("UNCHECKED_CAST")
interface MetadataController {

    fun apply(entities: List<Entity>, key: String, value: Any)
    fun apply(entity: Entity, key: String, value: Any)

    fun remove(entities: List<Entity>, key: String)
    fun remove(entity: Entity, key: String)

    fun has(entity: Entity, key: String): Boolean = entity.hasMetadata(key)

    fun <T> get(entity: Entity, key: String): T? = entity.getMetadata(key)[0].value() as T?

}