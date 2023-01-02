package love.chihuyu.utils

import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.game.PhaseData
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle

object BossbarUtil {

    fun removeBossbar(key: String) {
        plugin.server.getBossBar(NamespacedKey.fromString(key)!!)?.removeAll()
    }

    fun updateBossbar(startedEpoch: Long, secondsPerPhase: Long, phases: Int) {
        val phaseEndEpoch = startedEpoch + (PhaseData.elapsedPhases * secondsPerPhase)
        removeBossbar("bruh")

        val bossBar = Bukkit.createBossBar(
            NamespacedKey.fromString("bruh")!!,
            "フェーズ ${PhaseData.elapsedPhases}/$phases - ${EpochUtil.formatTime(phaseEndEpoch - EpochUtil.nowEpoch())}",
            BarColor.BLUE,
            BarStyle.SEGMENTED_6
        )

        // Avoidance exception "Progress must be between 0.0 and 1.0 (-0.05)"
        bossBar.progress = +(1.0 / secondsPerPhase) * +(phaseEndEpoch - EpochUtil.nowEpoch())
        bossBar.isVisible = true

        plugin.server.onlinePlayers.forEach {
            if (phaseEndEpoch - EpochUtil.nowEpoch() in 1..5) {
                it.playSound(it, Sound.UI_BUTTON_CLICK, 1f, 1f)
            }
            bossBar.addPlayer(it)
        }
    }
}