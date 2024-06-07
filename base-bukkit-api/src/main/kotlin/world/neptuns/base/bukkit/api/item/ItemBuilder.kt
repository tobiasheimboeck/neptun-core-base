package world.neptuns.base.bukkit.api.item

import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.core.base.api.utils.Builder

interface ItemBuilder : Builder<ItemStack> {

    fun displayName(displayName: Component): ItemBuilder
    fun lore(lore: List<Component>): ItemBuilder

    fun amount(amount: Int): ItemBuilder

    fun texture(value: String): ItemBuilder

    fun armorColor(color: Color): ItemBuilder

    fun armorTrim(trimMaterial: TrimMaterial, trimPattern: TrimPattern): ItemBuilder

    fun glow(): ItemBuilder

    fun enchantment(enchantment: Enchantment, power: Int): ItemBuilder

    fun flags(vararg flags: ItemFlag): ItemBuilder

    fun unbreakable(): ItemBuilder

    fun persistentData(key: String, value: Any): ItemBuilder

    companion object {
        fun skullBuilder() = builder(Material.PLAYER_HEAD)
        fun builder(type: Material) = NeptunCoreBukkitProvider.api.newItem(type)
    }

}