package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.POINT_HOPPER
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import love.chihuyu.config.ConfigKeys
import love.chihuyu.data.PhaseData
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetCategory
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import love.chihuyu.utils.ScoreboardUtil
import love.chihuyu.utils.runTaskLater
import love.chihuyu.utils.runTaskTimer
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import java.time.Instant

object ItemhuntStart {

    val main = CommandAPICommand("start")
        .executes(
            CommandExecutor { sender, args ->
                val phases = plugin.config.getInt(ConfigKeys.PHASES.key)
                val secondsPerPhase = plugin.config.getLong(ConfigKeys.PHASE_TIME.key)
                val materials = plugin.config.getList(ConfigKeys.MATERIALS.key)
                val targets = plugin.config.getInt(ConfigKeys.TARGETS.key)
                val startedEpoch = nowEpoch()
                val gameFinishEpoch = startedEpoch + (secondsPerPhase * phases)

                fun error(key: String) {
                    sender.sendMessage("$prefix ${key}が未設定です")
                    sender.sendMessage("$prefix /ih $key で設定を行ってください")
                }

                if (materials == null) {
                    error("materials")
                    return@CommandExecutor
                }

                if (targets == 0) {
                    error("targets")
                    return@CommandExecutor
                }

                if (secondsPerPhase == 0L) {
                    error("phase_time")
                    return@CommandExecutor
                }

                if (phases == 0) {
                    error("phases")
                    return@CommandExecutor
                }

                Itemhunt.started = true

                onGameStart()

                val taskUpdateBossbar = plugin.runTaskTimer(0, 20) {
                    val phaseEndEpoch = startedEpoch + (PhaseData.elapsedPhases * secondsPerPhase)
                    BossbarUtil.removeBossbar("bruh")

                    val bossBar = Bukkit.createBossBar(
                        NamespacedKey.fromString("bruh")!!,
                        "フェーズ ${PhaseData.elapsedPhases}/$phases - ${formatTime(phaseEndEpoch - nowEpoch())}",
                        BarColor.RED,
                        BarStyle.SEGMENTED_6
                    )

                    // Avoidance exception "Progress must be between 0.0 and 1.0 (-0.05)"
                    bossBar.progress = (1.0 / secondsPerPhase) * (phaseEndEpoch - nowEpoch()).unaryPlus()
                    bossBar.isVisible = true

                    plugin.server.onlinePlayers.forEach {
                        if (phaseEndEpoch - nowEpoch() in 1..5) {
                            it.playSound(it, Sound.UI_BUTTON_CLICK, 1f, 1f)
                        }
                        bossBar.addPlayer(it)
                    }
                }

                val taskUpdateTargetItem = plugin.runTaskTimer(0, secondsPerPhase * 20) {
                    PhaseData.elapsedPhases++

                    TargetItem.targetItem.clear()
                    repeat(targets) {
                        TargetItem.targetItem += TargetItem.data.filterKeys { it in materials.map { TargetCategory.valueOf(it.toString()) } }.values.flatMap { it.keys }.random()
                    }
                    ScoreboardUtil.updateServerScoreboard()

                    plugin.server.onlinePlayers.forEach { player ->
                        player.sendMessage("フェーズ${PhaseData.elapsedPhases}開始！")

                        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                    }
                }

                val taskGameEnd = plugin.runTaskLater((gameFinishEpoch - startedEpoch) * 20) {
                    taskUpdateBossbar.cancel()
                    taskUpdateTargetItem.cancel()

                    onGameEnd()
                }
            }
        )

    private fun onGameStart() {
        plugin.server.onlinePlayers.forEach {
            PlayerData.data[it.uniqueId] = mutableMapOf()
            it.gameMode = GameMode.SURVIVAL
            if (it.inventory.filterNotNull()
                    .none { item -> item.itemMeta?.hasCustomModelData() == true }
            ) it.inventory.addItem(POINT_HOPPER)
        }

        plugin.server.broadcastMessage(
            """
            ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
            ${" ".repeat(1)}
            アイテムハント開始！
            ${" ".repeat(2)}
            ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
            """.trimIndent()
        )
    }

    private fun onGameEnd() {
        BossbarUtil.removeBossbar("bruh")
        PhaseData.elapsedPhases = 0
        Itemhunt.started = false

        val sortedPlayerData =
            PlayerData.data.toList().sortedByDescending { it.second.map { scores -> scores.value }.sum() }
                .filterNot { it.second.values.sum() == 0 }

        plugin.server.onlinePlayers.forEach { player ->
            val yourRank = sortedPlayerData.map { it.first }.indexOf(player.uniqueId).inc()
            player.spigot().sendMessage(
                TextComponent(
                    """
                    ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
                    ${
                        if (sortedPlayerData.isNotEmpty()) {
                            """
                    アイテムハント終了！
                    勝者は${ChatColor.BOLD}${Bukkit.getOfflinePlayer(sortedPlayerData[0].first).name}${ChatColor.RESET}です
                    あなたは${if (yourRank == 0) "圏外" else "${yourRank}位"}でした
                    """
                        } else {
                            """
                    アイテムハント終了！
                    勝者はいませんでした！
                    """
                        }
                    }
                    ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
                    """.trimIndent()
                ).apply endMessage@{
                    this@endMessage.addExtra(
                        TextComponent("${ChatColor.UNDERLINE}ここにカーソルを合わせるとランキングが表示されます").apply rankingComponent@{
                            this@rankingComponent.hoverEvent = HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Text(sortedPlayerData.joinToString("\n") {
                                    "#${
                                        sortedPlayerData.indexOf(it).inc()
                                    } ${Bukkit.getOfflinePlayer(it.first).name} ${it.second.values.sum()}pt"
                                })
                            )
                        }
                    )
                }
            )

            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, .5f, 1f)
            player.gameMode = GameMode.ADVENTURE
        }

        TargetItem.targetItem.clear()
        ScoreboardUtil.updateServerScoreboard()
    }

    private fun formatTime(timeSeconds: Long): String {
        return "${"%02d".format(timeSeconds.floorDiv(3600))}:" + "${"%02d".format(timeSeconds.floorDiv(60))}:" + "%02d".format(
            timeSeconds % 60
        )
    }

    private fun nowEpoch(): Long {
        return Instant.now().epochSecond
    }
}