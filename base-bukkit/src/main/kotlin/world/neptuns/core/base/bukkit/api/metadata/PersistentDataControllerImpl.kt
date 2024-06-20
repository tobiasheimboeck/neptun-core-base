package world.neptuns.core.base.bukkit.api.metadata

import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import world.neptuns.base.bukkit.api.metadata.PersistentDataController
import world.neptuns.streamline.api.StreamlineApi

class PersistentDataControllerImpl(private val plugin: JavaPlugin) : PersistentDataController {

    override fun apply(itemMeta: ItemMeta, key: String, value: Any) {
        val namespacedKey = NamespacedKey(plugin, key)

        if (itemMeta.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) return
        itemMeta.persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, StreamlineApi.GSON.toJson(value))
    }

    override fun remove(itemMeta: ItemMeta, key: String) {
        val namespacedKey = NamespacedKey(plugin, key)

        if (!itemMeta.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) return
        itemMeta.persistentDataContainer.remove(namespacedKey)
    }

    override fun has(itemMeta: ItemMeta, key: String): Boolean {
        val namespacedKey = NamespacedKey(plugin, key)
        return itemMeta.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)
    }

    override fun <T> get(itemMeta: ItemMeta, key: String, clazz: Class<T>): T? {
        val namespacedKey = NamespacedKey(plugin, key)
        val jsonString = itemMeta.persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.STRING, "")
        return StreamlineApi.GSON.fromJson(jsonString, clazz)
    }

}