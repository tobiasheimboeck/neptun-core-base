package world.neptuns.core.base.common.api.language.color

import world.neptuns.core.base.api.currency.type.Crystals
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor

class LanguageColorImpl(
    override val name: LineKey,
    override val permission: String?,
    override val hexFormat: String,
    override val requiredCurrency: Crystals,
    override val price: Long
) : LanguageColor