package world.neptuns.core.base.common.database.properties.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import world.neptuns.core.base.common.database.properties.LanguagePropertiesTable

class LanguagePropertiesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LanguagePropertiesEntity>(LanguagePropertiesTable)

    var uuid by LanguagePropertiesTable.uuid
    var languageKey by LanguagePropertiesTable.languageKey
    var primaryColor by LanguagePropertiesTable.primaryColor
    var secondaryColor by LanguagePropertiesTable.secondaryColor
    var separatorColor by LanguagePropertiesTable.separatorColor
}