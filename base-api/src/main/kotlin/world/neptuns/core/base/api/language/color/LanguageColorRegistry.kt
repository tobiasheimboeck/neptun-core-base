package world.neptuns.core.base.api.language.color

import net.kyori.adventure.text.format.NamedTextColor
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.utils.ClassRegistry

class LanguageColorRegistry {

    object Custom {
        fun create(name: LineKey, permission: String?, hexFormat: String, description: LineKey, price: Long): LanguageColor {
            return NeptunCoreProvider.api.newLanguageColor(name, permission, hexFormat, description, price)
        }
    }

    object Official : ClassRegistry<LanguageColor> {
        override val elements: MutableSet<LanguageColor> = mutableSetOf()

        val RASPBERRY_BLAZE = Custom.create(LineKey.colorKey("raspberry_blaze"), null, "#FC004D", LineKey.colorDescriptionKey("raspberry_blaze"), 0)

        init {
            elements.addAll(listOf(RASPBERRY_BLAZE))
        }
    }

    object Default : ClassRegistry<LanguageColor> {
        override val elements: MutableSet<LanguageColor> = mutableSetOf()

        val BLACK = Custom.create(LineKey.colorKey("black"), null, NamedTextColor.BLACK.asHexString(), LineKey.colorDescriptionKey("black"), 0)
        val DARK_BLUE = Custom.create(LineKey.colorKey("dark_blue"), null, NamedTextColor.DARK_BLUE.asHexString(), LineKey.colorDescriptionKey("dark_blue"), 0)
        val DARK_GREEN = Custom.create(LineKey.colorKey("dark_green"), null, NamedTextColor.DARK_GREEN.asHexString(), LineKey.colorDescriptionKey("dark_green"), 0)
        val DARK_AQUA = Custom.create(LineKey.colorKey("dark_aqua"), null, NamedTextColor.DARK_AQUA.asHexString(), LineKey.colorDescriptionKey("dark_aqua"), 0)
        val DARK_RED = Custom.create(LineKey.colorKey("dark_red"), null, NamedTextColor.DARK_RED.asHexString(), LineKey.colorDescriptionKey("dark_red"), 0)
        val DARK_PURPLE = Custom.create(LineKey.colorKey("dark_purple"), null, NamedTextColor.DARK_PURPLE.asHexString(), LineKey.colorDescriptionKey("dark_purple"), 0)
        val GOLD = Custom.create(LineKey.colorKey("gold"), null, NamedTextColor.GOLD.asHexString(), LineKey.colorDescriptionKey("gold"), 0)
        val GRAY = Custom.create(LineKey.colorKey("gray"), null, NamedTextColor.GRAY.asHexString(), LineKey.colorDescriptionKey("gray"), 0)
        val DARK_GRAY = Custom.create(LineKey.colorKey("dark_gray"), null, NamedTextColor.DARK_GRAY.asHexString(), LineKey.colorDescriptionKey("dark_gray"), 0)
        val BLUE = Custom.create(LineKey.colorKey("blue"), null, NamedTextColor.BLUE.asHexString(), LineKey.colorDescriptionKey("blue"), 0)
        val GREEN = Custom.create(LineKey.colorKey("green"), null, NamedTextColor.GREEN.asHexString(), LineKey.colorDescriptionKey("green"), 0)
        val AQUA = Custom.create(LineKey.colorKey("aqua"), null, NamedTextColor.AQUA.asHexString(), LineKey.colorDescriptionKey("aqua"), 0)
        val RED = Custom.create(LineKey.colorKey("red"), null, NamedTextColor.RED.asHexString(), LineKey.colorDescriptionKey("red"), 0)
        val LIGHT_PURPLE = Custom.create(LineKey.colorKey("light_purple"), null, NamedTextColor.LIGHT_PURPLE.asHexString(), LineKey.colorDescriptionKey("light_purple"), 0)
        val YELLOW = Custom.create(LineKey.colorKey("yellow"), null, NamedTextColor.YELLOW.asHexString(), LineKey.colorDescriptionKey("yellow"), 0)
        val WHITE = Custom.create(LineKey.colorKey("white"), null, NamedTextColor.WHITE.asHexString(), LineKey.colorDescriptionKey("white"), 0)

        init {
            elements.addAll(listOf(
                BLACK,
                DARK_BLUE,
                DARK_GREEN,
                DARK_AQUA,
                DARK_RED,
                DARK_PURPLE,
                GOLD,
                GRAY,
                DARK_GRAY,
                BLUE,
                GREEN,
                AQUA,
                RED,
                LIGHT_PURPLE,
                YELLOW,
                WHITE
            ))
        }
    }

}