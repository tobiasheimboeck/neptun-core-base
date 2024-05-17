package world.neptuns.core.base.api.language.prefix

import net.kyori.adventure.text.format.TextColor

class PrefixFormatRegistry {

    class WrappingSquareBracketPrefix(separatorColor: TextColor) : PrefixFormat(separatorColor, SeparatorPosition.WRAPPING, '[', ']')
    class WrappingRoundBracketPrefix(separatorColor: TextColor) : PrefixFormat(separatorColor, SeparatorPosition.WRAPPING, '[', ']')
    class InlinePrefixFormat(separatorColor: TextColor) : PrefixFormat(separatorColor, SeparatorPosition.AFTER, '|')
    class LeadingTrailingSeparatorPrefix(separatorColor: TextColor) : PrefixFormat(separatorColor, SeparatorPosition.WRAPPING_WITH_SPACING, '>', '|')

    companion object {
        fun customFormat(separatorColor: TextColor, separatorPosition: PrefixFormat.SeparatorPosition, vararg symbols: Char): PrefixFormat {
            return PrefixFormat(separatorColor, separatorPosition, *symbols)
        }
    }

}