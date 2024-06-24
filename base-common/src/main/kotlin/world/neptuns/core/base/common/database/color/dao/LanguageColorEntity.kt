package world.neptuns.core.base.common.database.color.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import world.neptuns.core.base.common.database.color.LanguageColorTable

class LanguageColorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LanguageColorEntity>(LanguageColorTable)

    var uuid by LanguageColorTable.uuid
    var name by LanguageColorTable.name
    var hexFormat by LanguageColorTable.hexFormat
}