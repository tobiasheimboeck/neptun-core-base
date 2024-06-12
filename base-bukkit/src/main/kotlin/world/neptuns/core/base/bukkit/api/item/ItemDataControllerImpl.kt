package world.neptuns.core.base.bukkit.api.item

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import world.neptuns.base.bukkit.api.item.ItemDataController
import world.neptuns.streamline.api.StreamlineApi

class ItemDataControllerImpl(private val plugin: JavaPlugin) : ItemDataController {

    override fun hasData(itemMeta: ItemMeta, key: String): Boolean {
        val namespacedKey = NamespacedKey(this.plugin, key)
        return itemMeta.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)
    }

    override fun <T> getData(itemMeta: ItemMeta, key: String, clazz: Class<T>): T {
        val namespacedKey = NamespacedKey(this.plugin, key)
        val jsonString: String = itemMeta.persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.STRING, "")
        return StreamlineApi.GSON.fromJson(jsonString, clazz)
    }

    override fun setData(itemStack: ItemStack, itemMeta: ItemMeta, key: String, data: Any) {
        val namespacedKey = NamespacedKey(this.plugin, key)
        val dataContainer: PersistentDataContainer = itemMeta.persistentDataContainer
        if (dataContainer.has(namespacedKey, PersistentDataType.STRING)) return
        dataContainer.set(namespacedKey, PersistentDataType.STRING, StreamlineApi.GSON.toJson(data))
        itemStack.setItemMeta(itemMeta)
    }

}