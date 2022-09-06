package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ExecutorType
import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.data.PhaseData
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.*
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.scoreboard.DisplaySlot
import java.time.Instant

object SubCommandStart {
    val main: CommandAPICommand = CommandAPICommand("start")
        .withArguments(IntegerArgument("phases"), LongArgument("secondsPerPhase"))
        .executes(CommandExecutor { sender, args ->
            val phases = args[0] as Int
            val phaseTimeLimit = args[1] as Long
            val startedEpoch = nowEpoch()
            val gameFinishEpoch = startedEpoch + (phaseTimeLimit * phases)

            sender.sendMessage("Game has started!")
            Itemhunt.started = true

            plugin.server.onlinePlayers.forEach {
                PlayerData.data[it.uniqueId] = mutableMapOf()
            }

            val taskUpdateBossbar = plugin.runTaskTimer(0, 20) {
                val phaseEndEpoch = startedEpoch + (PhaseData.elapsedPhases * phaseTimeLimit)
                BossbarUtil.removeBossbar("bruh")

                val bossBar = Bukkit.createBossBar(
                    NamespacedKey.fromString("bruh")!!,
                    "フェーズ ${PhaseData.elapsedPhases}/$phases - ${formatTime(phaseEndEpoch - nowEpoch())}",
                    BarColor.RED,
                    BarStyle.SEGMENTED_6
                )

                bossBar.progress = (1.0 / phaseTimeLimit) * (phaseEndEpoch - nowEpoch())
                bossBar.isVisible = true

                plugin.server.onlinePlayers.forEach {
                    bossBar.addPlayer(it)
                }
            }

            val taskUpdateTargetItem = plugin.runTaskTimer(0, phaseTimeLimit * 20) {
                PhaseData.elapsedPhases++

                TargetItem.targetItem = Material.values().random()
                ScoreboardUtil.updateScoreboard()
            }

            val taskGameEnd = plugin.runTaskLater((gameFinishEpoch - startedEpoch) * 20) {
                BossbarUtil.removeBossbar("bruh")
                PhaseData.elapsedPhases = 0
                taskUpdateBossbar.cancel()
                taskUpdateTargetItem.cancel()
                Itemhunt.started = false
                clearScoreboard()
            }
        })


    private fun formatTime(timeSeconds: Long): String {
        return "${"%02d".format(timeSeconds.floorDiv(3600))}:${"%02d".format(timeSeconds.floorDiv(60))}:${"%02d".format(timeSeconds % 60)}"
    }

    private fun nowEpoch(): Long {
        return Instant.now().epochSecond
    }

    private fun clearScoreboard() {
        plugin.server.onlinePlayers.forEach { it.scoreboard.clearSlot(DisplaySlot.SIDEBAR) }
    }
}