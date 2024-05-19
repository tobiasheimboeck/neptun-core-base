package world.neptuns.core.base.common.api.language.color

import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.currency.type.Crystals

class LanguageColorImpl(
    override val name: LineKey,
    override val permission: String?,
    override val hexFormat: String,
    override val requiredCurrency: Crystals,
    override val description: LineKey,
    override val price: Long
) : LanguageColor