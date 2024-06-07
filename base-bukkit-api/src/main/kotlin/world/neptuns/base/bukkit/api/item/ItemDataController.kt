package world.neptuns.base.bukkit.api.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

interface ItemDataController {

    fun hasData(itemMeta: ItemMeta, key: String): Boolean

    fun <T> getData(itemMeta: ItemMeta, key: String, clazz: Class<T>): T

    fun setData(itemStack: ItemStack, itemMeta: ItemMeta, key: String, data: Any)

}