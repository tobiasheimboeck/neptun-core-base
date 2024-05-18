package world.neptuns.core.base.api.language.prefix

open class PrefixFormat(val separatorPosition: SeparatorPosition, vararg val symbols: Char) {

    fun asString(): String {
        return when (this.separatorPosition) {
            SeparatorPosition.WRAPPING -> "${this.symbols[0]}<prefix_text>${this.symbols[1]}"
            SeparatorPosition.WRAPPING_WITH_SPACING -> "${this.symbols[0]} <prefix_text> ${this.symbols[1]}"
            SeparatorPosition.BEFORE -> "${this.symbols[0]} <prefix_text>"
            SeparatorPosition.AFTER -> "<prefix_text> ${this.symbols[0]}"
        }
    }

    enum class SeparatorPosition {
        WRAPPING,
        WRAPPING_WITH_SPACING,
        AFTER,
        BEFORE;
    }
}