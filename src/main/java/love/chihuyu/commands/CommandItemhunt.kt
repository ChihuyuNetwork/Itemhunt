package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.ListArgumentBuilder
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.data.PhaseData
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetCategory
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import love.chihuyu.utils.ScoreboardUtil
import love.chihuyu.utils.runTaskLater
import love.chihuyu.utils.runTaskTimer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import java.time.Instant

object CommandItemhunt {
    val main = CommandAPICommand("itemhunt")
        .withSubcommand(
            CommandAPICommand("start")
                .withArguments(
                    IntegerArgument("phases"),
                    LongArgument("secondsPerPhase"),
                    IntegerArgument("targets"),
                    ListArgumentBuilder<TargetCategory>("materials").allowDuplicates(false)
                        .withList(TargetCategory.values().toList()).withStringMapper().build()
                )
                .executes(
                    CommandExecutor { sender, args ->
                        val phases = args[0] as Int
                        val secondsPerPhase = args[1] as Long
                        val materials = mutableMapOf<Material, Int>()
                        val targets = args[2] as Int
                        val startedEpoch = nowEpoch()
                        val gameFinishEpoch = startedEpoch + (secondsPerPhase * phases)

                        (args[3] as List<TargetCategory>).forEach { category ->
                            TargetItem.targetData[category]?.forEach { (material, score) ->
                                materials[material] = score
                            }
                        }

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

                            plugin.server.onlinePlayers.forEach {
                                if (phaseEndEpoch - nowEpoch() in 1..4) {
                                    it.playSound(it, Sound.UI_BUTTON_CLICK, 1f, 1f)
                                }
                                bossBar.addPlayer(it)
                            }
                        }

                        val taskUpdateTargetItem = Itemhunt.plugin.runTaskTimer(0, secondsPerPhase * 20) {
                            PhaseData.elapsedPhases++

                            TargetItem.targetItem.clear()
                            repeat(targets) {
                                TargetItem.targetItem += materials.keys.random()
                            }
                            ScoreboardUtil.updateScoreboard()

                            plugin.server.onlinePlayers.forEach {
                                it.playSound(it, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                            }
                        }

                        val taskGameEnd = plugin.runTaskLater((gameFinishEpoch - startedEpoch) * 20) {
                            taskUpdateBossbar.cancel()
                            taskUpdateTargetItem.cancel()

                            onGameEnd()
                        }
                    }
                )
        )
        .withPermission(CommandPermission.OP)
        .withAliases("ih")

    private fun onGameStart() {
        plugin.server.onlinePlayers.forEach {
            PlayerData.data[it.uniqueId] = mutableMapOf()
        }

        plugin.server.broadcastMessage(
            """
            ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(24)}${ChatColor.RESET}
            
            ゲーム開始！
            
            ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(24)}${ChatColor.RESET}
            """.trimIndent()
        )
    }

    private fun onGameEnd() {
        BossbarUtil.removeBossbar("bruh")
        PhaseData.elapsedPhases = 0
        Itemhunt.started = false

        val sortedPlayerData = PlayerData.data.toList().sortedByDescending { it.second.map { it.value }.sum() }

        plugin.server.onlinePlayers.forEach { player ->
            player.sendMessage(
                """
            ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(24)}${ChatColor.RESET}
            
            ゲーム終了！
            勝者は${Bukkit.getOfflinePlayer(sortedPlayerData[0].first).name}です
            あなたは${sortedPlayerData.map { it.first }.indexOf(player.uniqueId).inc()}位でした
             
            ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(24)}${ChatColor.RESET}
            """.trimIndent())
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