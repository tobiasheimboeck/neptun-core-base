package world.neptuns.core.base.common.api.language.color

import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import world.neptuns.core.base.api.NeptunCoreProvider
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.color.LanguageColor
import world.neptuns.core.base.api.language.color.LanguageColorController
import world.neptuns.core.base.api.player.NeptunOfflinePlayer
import world.neptuns.core.base.common.repository.color.LanguageColorRepository
import world.neptuns.core.base.common.repository.color.LanguageColorTable
import java.util.*

class LanguageColorControllerImpl : LanguageColorController {

    private val languageColorRepository = NeptunCoreProvider.api.repositoryLoader.get(LanguageColorRepository::class.java)!!

    override suspend fun getColorAsync(uuid: UUID, name: LineKey): Deferred<LanguageColor?> {
        return this.languageColorRepository.getValue(uuid) { it.name.asString() == name.asString() }
    }

    override suspend fun getColorsAsync(uuid: UUID): Deferred<List<LanguageColor>> {
        return this.languageColorRepository.getValues(uuid)
    }

    override suspend fun buyOrSelectColor(offlinePlayer: NeptunOfflinePlayer, languageColor: LanguageColor) {
        val transactionStatus = languageColor.buy(offlinePlayer)
        if (!transactionStatus) return

        newSuspendedTransaction {
            LanguageColorTable.insert {
                it[this.uuid] = uuid
                it[this.name] = name
                it[this.hexFormat] = hexFormat
            }
        }

        this.languageColorRepository.insert(offlinePlayer.uuid, languageColor)
    }

    override suspend fun loadColors(uuid: UUID) {

    }

    override suspend fun unloadColors(uuid: UUID) {

    }

}