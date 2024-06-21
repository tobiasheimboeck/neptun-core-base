package world.neptuns.base.bukkit.api.metadata

import org.bukkit.inventory.meta.ItemMeta

interface PersistentDataController {

    fun apply(itemMeta: ItemMeta, key: String, value: Any)

    fun remove(itemMeta: ItemMeta, key: String)

    fun has(itemMeta: ItemMeta, key: String): Boolean

    fun <T> get(itemMeta: ItemMeta, key: String, clazz: Class<T>): T?

}