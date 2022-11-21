package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import love.chihuyu.config.ConfigKeys
import love.chihuyu.data.PhaseData
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.*
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ItemhuntStart {

    val main = CommandAPICommand("start")
        .executes(
            CommandExecutor { sender, args ->
                val phases = plugin.config.getInt(ConfigKeys.PHASES.key)
                val secondsPerPhase = plugin.config.getLong(ConfigKeys.PHASE_TIME.key)
                val materials = plugin.config.getList(ConfigKeys.MATERIALS.key)
                val targets = plugin.config.getInt(ConfigKeys.TARGETS.key)
                val nightVision = plugin.config.getBoolean(ConfigKeys.NIGHT_VISION.key)
                val startedEpoch = EpochUtil.nowEpoch()
                val gameFinishEpoch = startedEpoch + (secondsPerPhase * phases)

                fun onGameStart() {
                    plugin.server.onlinePlayers.forEach {
                        PlayerData.data[it.uniqueId] = mutableMapOf()
                        it.gameMode = GameMode.SURVIVAL
                        ItemUtil.addPointHopperIfHavent(it)
                        if (nightVision) it.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0, false, false, true))
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

                fun onGameEnd() {
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
                                val hoverEvent = HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Text(sortedPlayerData.joinToString("\n") { "#${sortedPlayerData.indexOf(it).inc()} ${Bukkit.getOfflinePlayer(it.first).name} ${it.second.values.sum()}pt" })
                                )
                                val textComponent = TextComponent("${ChatColor.UNDERLINE}ここにカーソルを合わせるとランキングが表示されます").apply { this.hoverEvent = hoverEvent }
                                this@endMessage.addExtra(textComponent)
                            }
                        )

                        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, .5f, 1f)
                        player.gameMode = GameMode.ADVENTURE
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION)
                    }

                    TargetItem.activeTarget.clear()
                    PlayerData.data.clear()
                    ScoreboardUtil.updateServerScoreboard()
                }

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

                val taskTickGame = plugin.runTaskTimer(0, 20) {
                    BossbarUtil.updateBossbar(startedEpoch, secondsPerPhase, phases)
                }

                val taskUpdateActiveItem = plugin.runTaskTimer(0, secondsPerPhase * 20) {
                    PhaseData.elapsedPhases++

                    TargetItem.activeTarget.clear()
                    repeat(targets) {
                        TargetItem.activeTarget += TargetItem.data.filterKeys {
                            it in materials.map { material -> material.toString() }
                        }.values.flatMap { it.keys }.random()
                    }
                    ScoreboardUtil.updateServerScoreboard()

                    plugin.server.onlinePlayers.forEach { player ->
                        player.sendMessage("フェーズ${PhaseData.elapsedPhases}開始！")
                        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
                    }
                }

                val taskGameEnd = plugin.runTaskLater((gameFinishEpoch - startedEpoch) * 20) {
                    taskTickGame.cancel()
                    taskUpdateActiveItem.cancel()

                    onGameEnd()
                }
            }
        )
}