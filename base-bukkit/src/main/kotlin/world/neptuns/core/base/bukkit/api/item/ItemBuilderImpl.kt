package world.neptuns.core.base.bukkit.api.item

import com.destroystokyo.paper.profile.ProfileProperty
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import world.neptuns.base.bukkit.api.NeptunCoreBukkitProvider
import world.neptuns.base.bukkit.api.item.ItemBuilder
import java.util.*

class ItemBuilderImpl(private val type: Material) : ItemBuilder {

    private var itemStack = ItemStack(this.type)

    override fun displayName(displayName: Component): ItemBuilder {
        this.itemStack.editMeta { it.displayName(displayName) }
        return this
    }

    override fun lore(lore: List<Component>): ItemBuilder {
        this.itemStack.editMeta { it.lore(lore) }
        return this
    }

    override fun amount(amount: Int): ItemBuilder {
        this.itemStack.amount = amount
        return this
    }

    override fun texture(value: String): ItemBuilder {
        val profile = Bukkit.createProfile(UUID.randomUUID().toString().split("-")[0])
        profile.setProperty(ProfileProperty("textures", value))

        this.itemStack.editMeta {
            val skullMeta = it as SkullMeta
            skullMeta.playerProfile = profile
        }

        return this
    }

    override fun armorColor(color: Color): ItemBuilder {
        this.itemStack.editMeta {
            val armorMeta = it as LeatherArmorMeta
            armorMeta.setColor(color)
        }

        return this
    }

    override fun armorTrim(trimMaterial: TrimMaterial, trimPattern: TrimPattern): ItemBuilder {
        this.itemStack.editMeta {
            val armorMeta = it as ArmorMeta
            armorMeta.trim = ArmorTrim(trimMaterial, trimPattern)
        }

        return this
    }

    override fun glow(): ItemBuilder {
        this.itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
        return this
    }

    override fun enchantment(enchantment: Enchantment, power: Int): ItemBuilder {
        this.itemStack.editMeta { it.addEnchant(enchantment, power, true) }
        return this
    }

    override fun flags(vararg flags: ItemFlag): ItemBuilder {
        this.itemStack.editMeta { it.addItemFlags(*flags) }
        return this
    }

    override fun unbreakable(): ItemBuilder {
        this.itemStack.editMeta { it.isUnbreakable = true }
        return this
    }

    override fun persistentData(key: String, value: Any): ItemBuilder {
        this.itemStack.editMeta { NeptunCoreBukkitProvider.api.itemDataController.setData(this.itemStack, it, key, value) }
        return this
    }

    override fun build(): ItemStack {
        return this.itemStack
    }

}