package world.neptuns.core.base.api.language.prefix

import net.kyori.adventure.text.format.TextColor

open class PrefixFormat(val separatorColor: TextColor, val separatorPosition: SeparatorPosition, vararg val symbols: Char) {
    enum class SeparatorPosition() {
        WRAPPING,
        WRAPPING_WITH_SPACING,
        AFTER,
        BEFORE
    }
}