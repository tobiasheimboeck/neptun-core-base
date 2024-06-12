package world.neptuns.core.base.api.extension

import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.properties.LanguageProperties
import world.neptuns.core.base.api.player.NeptunOfflinePlayer

suspend fun NeptunOfflinePlayer.getLanguage(): Language? {
    val languageProperties = getLanguageProperties() ?: return null
    return NeptunCoreProvider.api.languageController.getLanguage(languageProperties.langKey)
}

suspend fun NeptunOfflinePlayer.getPrimaryColor(): LanguageColor? {
    val languageProperties = getLanguageProperties() ?: return null
    return languageProperties.primaryColor
}

suspend fun NeptunOfflinePlayer.getSecondaryColor(): LanguageColor? {
    val languageProperties = getLanguageProperties() ?: return null
    return languageProperties.secondaryColor
}

suspend fun NeptunOfflinePlayer.getSeparatorColor(): LanguageColor? {
    val languageProperties = getLanguageProperties() ?: return null
    return languageProperties.separatorColor
}

suspend fun NeptunOfflinePlayer.getLanguageProperties(): LanguageProperties? {
    return NeptunCoreProvider.api.languagePropertiesService.getProperties(this.uuid)
}