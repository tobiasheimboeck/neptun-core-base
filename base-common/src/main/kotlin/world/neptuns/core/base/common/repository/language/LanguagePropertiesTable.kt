package world.neptuns.core.base.common.repository.language

import org.jetbrains.exposed.sql.Table

object LanguagePropertiesTable : Table("language_properties") {
    val uuid = uuid("uuid")
    val languageKey = varchar("language_key", 30)
    val primaryColor = varchar("primary_color", 7)
    val secondaryColor = varchar("secondary_color", 7)
    val separatorColor = varchar("separator_color", 7)

    override val primaryKey: PrimaryKey = PrimaryKey(this.uuid)
}