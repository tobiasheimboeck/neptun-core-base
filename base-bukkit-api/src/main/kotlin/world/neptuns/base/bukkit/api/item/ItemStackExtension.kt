package world.neptuns.base.bukkit.api.item

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider

inline fun itemStack(material: Material, builder: ItemStack.() -> Unit) = ItemStack(material).apply(builder)

inline fun <reified T : ItemMeta> ItemStack.meta(builder: T.() -> Unit) {
    val curMeta = itemMeta as? T
    itemMeta = if (curMeta != null) {
        curMeta.apply(builder)
        curMeta
    } else {
        itemMeta(type, builder)
    }
}

@JvmName("simpleMeta")
inline fun ItemStack.meta(builder: ItemMeta.() -> Unit) = meta<ItemMeta>(builder)

inline fun <reified T : ItemMeta> ItemStack.setMeta(builder: T.() -> Unit) {
    itemMeta = itemMeta(type, builder)
}

@JvmName("simpleSetMeta")
inline fun ItemStack.setMeta(builder: ItemMeta.() -> Unit) = setMeta<ItemMeta>(builder)

inline fun <reified T : ItemMeta> itemMeta(material: Material, builder: T.() -> Unit): T? {
    val meta = Bukkit.getItemFactory().getItemMeta(material)
    return if (meta is T) meta.apply(builder) else null
}

@JvmName("simpleItemMeta")
inline fun itemMeta(material: Material, builder: ItemMeta.() -> Unit) = itemMeta<ItemMeta>(material, builder)

inline fun ItemMeta.setLore(builder: ItemMetaLoreBuilder.() -> Unit) {
    lore(ItemMetaLoreBuilder().apply(builder).components)
}

inline fun ItemMeta.addLore(builder: ItemMetaLoreBuilder.() -> Unit) {
    val newLore = lore() ?: mutableListOf<Component>()
    newLore.addAll(ItemMetaLoreBuilder().apply(builder).components)
    lore(newLore)
}

class ItemMetaLoreBuilder {
    val components = ArrayList<Component>()

    operator fun Component.unaryPlus() {
        components += this
    }

    operator fun String.unaryPlus() {
        components += text(this)
    }
}

fun ItemMeta.flag(itemFlag: ItemFlag) = addItemFlags(itemFlag)

fun ItemMeta.flags(vararg itemFlag: ItemFlag) = addItemFlags(*itemFlag)

fun ItemMeta.removeFlag(itemFlag: ItemFlag) = removeItemFlags(itemFlag)

fun ItemMeta.removeFlags(vararg itemFlag: ItemFlag) = removeItemFlags(*itemFlag)

fun ItemMeta.applyPersistentData(key: String, value: Any) =
    NeptunCoreBukkitProvider.api.persistentDataController.apply(this, key, value)

fun <T> ItemMeta.getPersistentDataValue(key: String, clazz: Class<T>): T? =
    NeptunCoreBukkitProvider.api.persistentDataController.get(this, key, clazz)

var ItemMeta.name: Component?
    get() = if (hasDisplayName()) displayName() else null
    set(value) = displayName(value ?: Component.space())

var ItemMeta.customModel: Int?
    get() = if (hasCustomModelData()) customModelData else null
    set(value) = setCustomModelData(value)