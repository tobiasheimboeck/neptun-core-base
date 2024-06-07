package world.neptuns.base.bukkit.api.metadata

import org.bukkit.entity.LivingEntity

@Suppress("UNCHECKED_CAST")
interface MetadataController {

    fun apply(entity: LivingEntity, key: String, value: Any)

    fun remove(entity: LivingEntity, key: String)

    fun has(entity: LivingEntity, key: String): Boolean = entity.hasMetadata(key)

    fun <T> get(entity: LivingEntity, key: String): T? = entity.getMetadata(key)[0].value() as T?

}