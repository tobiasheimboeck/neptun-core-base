package world.neptuns.core.base.bukkit.api.bossbar

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import org.bukkit.entity.Player
import world.neptuns.base.bukkit.api.bossbar.BossbarController
import java.util.*

class BossbarControllerImpl : BossbarController {

    override val bossbars: Multimap<UUID, Pair<String, BossBar>> = ArrayListMultimap.create()

    override fun getBossbar(uuid: UUID, key: String): BossBar? {
        return this.bossbars.get(uuid).find { it.first == key }?.second
    }

    override fun getBossbars(uuid: UUID): List<Pair<String, BossBar>> {
        return this.bossbars[uuid].toList()
    }

    override fun updateBossbar(uuid: UUID, key: String, updateType: BossbarController.Update, newValue: Any) {
        val bossbar: BossBar = getBossbar(uuid, key) ?: return
        when (updateType) {
            BossbarController.Update.NAME -> bossbar.name(newValue as ComponentLike)
            BossbarController.Update.PROGRESS -> bossbar.progress(newValue as Float)
            BossbarController.Update.COLOR -> bossbar.color(newValue as BossBar.Color)
        }
    }

    override fun registerBossbar(player: Player, key: String, name: Component, progress: Float, color: BossBar.Color, style: BossBar.Overlay) {
        val bossbar: BossBar = BossBar.bossBar(name, progress, color, style)
        this.bossbars.put(player.uniqueId, Pair(key, bossbar))
        player.showBossBar(bossbar)
    }

    override fun unregisterBossbar(player: Player, key: String) {
        val bossbar: BossBar = getBossbar(player.uniqueId, key) ?: return
        this.bossbars.entries().removeIf { it.key == player.uniqueId && it.value.first.equals(key, true) }
        player.hideBossBar(bossbar)
    }

    override fun clearBossbars(player: Player) {
        getBossbars(player.uniqueId).forEach { unregisterBossbar(player, it.first) }
    }

}