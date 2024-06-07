package world.neptuns.base.bukkit.api.bossbar

import com.google.common.collect.Multimap
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.Color
import net.kyori.adventure.bossbar.BossBar.Overlay
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*

interface BossbarController {

    val bossbars: Multimap<UUID, Pair<String, BossBar>>

    fun getBossbar(uuid: UUID, key: String): BossBar?
    fun getBossbars(uuid: UUID): List<Pair<String, BossBar>>

    fun updateBossbar(uuid: UUID, key: String, updateType: Update, newValue: Any)

    fun registerBossbar(player: Player, key: String, name: Component, progress: Float, color: Color, style: Overlay)
    fun unregisterBossbar(player: Player, key: String)

    fun clearBossbars(player: Player)

    enum class Update {
        NAME,
        PROGRESS,
        COLOR
    }

}