package world.neptuns.core.base.common.database.color

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import world.neptuns.core.base.common.database.player.OfflinePlayerTable

object LanguageColorTable : IntIdTable("language_colors") {
    val uuid = uuid("uuid").references(OfflinePlayerTable.uuid, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val hexFormat = varchar("hexFormat", 7)
}