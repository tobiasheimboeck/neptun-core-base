package world.neptuns.core.base.api.language.prefix

open class PrefixFormat(val separatorPosition: SeparatorPosition, vararg val symbols: Char) {
    enum class SeparatorPosition() {
        WRAPPING,
        WRAPPING_WITH_SPACING,
        AFTER,
        BEFORE
    }
}