package world.neptuns.core.base.common.repository.color

import org.jetbrains.exposed.sql.Table

object LanguageColorTable : Table("language_colors") {
    val uuid = uuid("uuid")
    val name = varchar("name", 255)
    val hexFormat = varchar("hexFormat", 7)
}