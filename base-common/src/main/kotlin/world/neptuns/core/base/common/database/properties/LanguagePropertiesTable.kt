package world.neptuns.core.base.common.database.properties

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import world.neptuns.core.base.common.database.player.OfflinePlayerTable

object LanguagePropertiesTable : IntIdTable("language_properties") {
    val uuid = uuid("uuid").references(OfflinePlayerTable.uuid, onDelete = ReferenceOption.CASCADE)
    val languageKey = varchar("language_key", 30)
    val primaryColor = varchar("primary_color", 7)
    val secondaryColor = varchar("secondary_color", 7)
    val separatorColor = varchar("separator_color", 7)
}