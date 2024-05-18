package world.neptuns.core.base.common.api.language

import world.neptuns.core.base.api.language.LangKey

class LangKeyImpl(
    override val countryCode: String,
    override val languageCode: String
) : LangKey