package world.neptuns.core.base.common.api.language

import world.neptuns.core.base.api.language.LanguageKey

class LanguageKeyImpl(
    override val countryCode: String,
    override val languageCode: String
) : LanguageKey