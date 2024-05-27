package world.neptuns.core.base.api.language.prefix

import world.neptuns.streamline.api.utils.ClassRegistry

class PrefixFormatRegistry {

    object Custom {
        fun create(separatorPosition: PrefixFormat.SeparatorPosition, vararg symbols: Char): PrefixFormat {
            return PrefixFormat(separatorPosition, *symbols)
        }
    }

    object Official : ClassRegistry<PrefixFormat> {
        override val elements: MutableSet<PrefixFormat> = mutableSetOf()

        val WRAPPING_SQUARE_BRACKET_PREFIX_FORMAT = PrefixFormat(PrefixFormat.SeparatorPosition.WRAPPING, '[', ']')

        init {
            elements.addAll(listOf(WRAPPING_SQUARE_BRACKET_PREFIX_FORMAT))
        }
    }

    object Bracket : ClassRegistry<PrefixFormat> {
        override val elements: MutableSet<PrefixFormat> = mutableSetOf()

        val WRAPPING_SQUARE_BRACKET_PREFIX_FORMAT = PrefixFormat(PrefixFormat.SeparatorPosition.WRAPPING, '[', ']')
        val WRAPPING_ROUND_BRACKET_PREFIX_FORMAT = PrefixFormat(PrefixFormat.SeparatorPosition.WRAPPING, '(', ')')
        val INLINE_PREFIX_FORMAT = PrefixFormat(PrefixFormat.SeparatorPosition.AFTER, '|')
        val LEADING_TRAILING_SEPARATOR_PREFIX = PrefixFormat(PrefixFormat.SeparatorPosition.WRAPPING_WITH_SPACING, '>', '|')

        init {
            elements.addAll(listOf(
                WRAPPING_SQUARE_BRACKET_PREFIX_FORMAT,
                WRAPPING_ROUND_BRACKET_PREFIX_FORMAT,
                INLINE_PREFIX_FORMAT,
                LEADING_TRAILING_SEPARATOR_PREFIX
            ))
        }
    }

}