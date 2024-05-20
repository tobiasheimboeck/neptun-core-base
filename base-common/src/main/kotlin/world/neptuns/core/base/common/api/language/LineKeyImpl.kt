package world.neptuns.core.base.common.api.language

import world.neptuns.core.base.api.language.LangNamespace
import world.neptuns.core.base.api.language.LineKey

class LineKeyImpl(override val namespace: LangNamespace, override val value: String) : LineKey