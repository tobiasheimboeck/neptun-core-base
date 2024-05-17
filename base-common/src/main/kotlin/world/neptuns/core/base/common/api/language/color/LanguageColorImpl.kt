package world.neptuns.core.base.common.api.language.color

import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.player.currency.CurrencyRegistry

class LanguageColorImpl(
    override val name: String,
    override val permission: String?,
    override val hexFormat: String,
    override val requiredCurrency: CurrencyRegistry.Crystals,
    override val description: LineKey,
    override val price: Long
) : LanguageColor