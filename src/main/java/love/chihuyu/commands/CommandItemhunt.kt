package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt
import love.chihuyu.data.PhaseData
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import love.chihuyu.utils.ScoreboardUtil
import love.chihuyu.utils.runTaskLater
import love.chihuyu.utils.runTaskTimer
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import java.time.Instant

object CommandItemhunt {
    val main = CommandAPICommand("itemhunt")
        .withSubcommand(CommandAPICommand("start")
            .withArguments(IntegerArgument("phases"), LongArgument("secondsPerPhase"))
            .executes(CommandExecutor { sender, args ->
                val phases = args[0] as Int
                val secondsPerPhase = args[1] as Long
                val startedEpoch = nowEpoch()
                val gameFinishEpoch = startedEpoch + (secondsPerPhase * phases)

                Itemhunt.started = true

                onGameStart()

                val taskUpdateBossbar = Itemhunt.plugin.runTaskTimer(0, 20) {
                    val phaseEndEpoch = startedEpoch + (PhaseData.elapsedPhases * secondsPerPhase)
                    BossbarUtil.removeBossbar("bruh")

                    val bossBar = Bukkit.createBossBar(
                        NamespacedKey.fromString("bruh")!!,
                        "フェーズ ${PhaseData.elapsedPhases}/$phases - ${formatTime(phaseEndEpoch - nowEpoch())}",
                        BarColor.RED,
                        BarStyle.SEGMENTED_6
                    )

                    bossBar.progress = (1.0 / secondsPerPhase) * (phaseEndEpoch - nowEpoch())
                    bossBar.isVisible = true

                    Itemhunt.plugin.server.onlinePlayers.forEach {
                        if (phaseEndEpoch - nowEpoch() in 1..4) {
                            it.playSound(it, Sound.UI_BUTTON_CLICK, 1f, 1f)
                        }
                        bossBar.addPlayer(it)
                    }
                }

                val taskUpdateTargetItem = Itemhunt.plugin.runTaskTimer(0, secondsPerPhase * 20) {
                    PhaseData.elapsedPhases++

                    TargetItem.targetItem = TargetItem.targetData.random()
                    ScoreboardUtil.updateScoreboard()

                    Itemhunt.plugin.server.onlinePlayers.forEach {
                        it.playSound(it, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                    }
                }

                val taskGameEnd = Itemhunt.plugin.runTaskLater((gameFinishEpoch - startedEpoch) * 20) {
                    taskUpdateBossbar.cancel()
                    taskUpdateTargetItem.cancel()

                    onGameEnd()
                }
            })
        )
        .withPermission(CommandPermission.OP)
        .withAliases("ih")

    private fun onGameStart() {
        Itemhunt.plugin.server.onlinePlayers.forEach {
            PlayerData.data[it.uniqueId] = mutableMapOf()
        }

        Itemhunt.plugin.server.broadcastMessage("ゲーム開始！")
        Itemhunt.plugin.server.worlds.forEach { it.setGameRule(GameRule.FALL_DAMAGE, false) }
    }

    private fun onGameEnd() {
        BossbarUtil.removeBossbar("bruh")
        PhaseData.elapsedPhases = 0
        Itemhunt.started = false

        val sortedPlayerData = PlayerData.data.toList().sortedByDescending { it.second.map { it.value }.sum() }

        Itemhunt.plugin.server.broadcastMessage("ゲーム終了！")
        Itemhunt.plugin.server.broadcastMessage("勝者は${Bukkit.getOfflinePlayer(sortedPlayerData[0].first).name}です")
        Itemhunt.plugin.server.onlinePlayers.forEach { player ->
            player.sendMessage("あなたは${sortedPlayerData.map { it.first }.indexOf(player.uniqueId).inc()}位でした")
            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, .5f, 1f)
        }
    }

    private fun formatTime(timeSeconds: Long): String {
        return "${"%02d".format(timeSeconds.floorDiv(3600))}:" +
                "${"%02d".format(timeSeconds.floorDiv(60))}:" +
                "%02d".format(timeSeconds % 60)
    }

    private fun nowEpoch(): Long {
        return Instant.now().epochSecond
    }
}