package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import love.chihuyu.config.ConfigKeys
import love.chihuyu.data.PhaseData
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import org.bukkit.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ItemhuntStart {

    val main: CommandAPICommand = CommandAPICommand("start").executesPlayer(PlayerCommandExecutor { sender, args ->
            val phases = plugin.config.getInt(ConfigKeys.PHASES.key)
            val secondsPerPhase = plugin.config.getLong(ConfigKeys.PHASE_TIME.key)
            val materials = plugin.config.getList(ConfigKeys.MATERIALS.key)
            val targets = plugin.config.getInt(ConfigKeys.TARGETS.key)
            val nightVision = plugin.config.getBoolean(ConfigKeys.NIGHT_VISION.key)
            val keepInventory = plugin.config.getBoolean(ConfigKeys.KEEP_INVENTORY.key)
            val tpAfterStart = plugin.config.getBoolean(ConfigKeys.TP_AFTER_START.key)
            val clearItem = plugin.config.getBoolean(ConfigKeys.CLEAR_ITEM.key)
            val startedEpoch = EpochUtil.nowEpoch()
            val gameFinishEpoch = startedEpoch + (secondsPerPhase * phases)

            fun onGameStart() {
                plugin.server.onlinePlayers.forEach { player ->
                    player.gameMode = GameMode.SURVIVAL
                    ItemUtil.addPointHopperIfHavent(player)
                    if (nightVision) player.addPotionEffect(
                        PotionEffect(
                            PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0, false, false, true
                        )
                    )
                    if (tpAfterStart) player.teleport(sender.location)
                    plugin.server.worlds.forEach { world ->
                        world.setGameRule(
                            GameRule.KEEP_INVENTORY, keepInventory
                        )
                    }
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

                val sortedPhasesData = PlayerData.phases.toList().sortedByDescending { it.second }
                val sortedPointsData = PlayerData.points.toList()
                    .sortedByDescending { it.second.sumOf { phase -> phase.values.sum() } }
                    .filterNot { it.second.sumOf { phase -> phase.values.sum() } == 0 }

                plugin.server.onlinePlayers.forEach { player ->
                    val yourRank = sortedPhasesData.map { it.first }.indexOf(player.uniqueId).inc()
                    player.sendMessage(Component.text(
                        """
                                ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
                                    ${
                            if (sortedPhasesData.isNotEmpty()) {
                                """
                                アイテムハント終了！
                                勝者は${ChatColor.BOLD}${Bukkit.getOfflinePlayer(sortedPhasesData[0].first).name}${ChatColor.RESET}です
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
                        val hoverEvent = HoverEventSource {
                            HoverEvent.showText(Component.text(sortedPhasesData.joinToString(
                                "\n"
                            ) {
                                "#${
                                    sortedPhasesData.indexOf(it).inc()
                                } ${Bukkit.getOfflinePlayer(it.first).name} ${it.second}Win(${
                                    sortedPointsData.first { p -> p.first == it.first }.second.sumOf { phase -> phase.values.sum() }
                                }pt)"
                            }))
                        }
                        val textComponent = Component.text("${ChatColor.UNDERLINE}ここにカーソルを合わせるとランキングが表示されます")
                            .apply { this.hoverEvent(hoverEvent) }
                        this@endMessage.append(textComponent)
                    })

                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, .5f, 1f)
                    player.gameMode = GameMode.ADVENTURE
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION)
                    if (clearItem) player.inventory.clear()
                }

                TargetItem.activeTarget.clear()
                PlayerData.points.clear()
                ScoreboardUtil.updateServerScoreboard()
            }

            fun error(key: String) {
                sender.sendMessage("$prefix ${key}が未設定です")
                sender.sendMessage("$prefix /ih $key で設定を行ってください")
            }

            if (materials == null) {
                error("materials")
                return@PlayerCommandExecutor
            }

            if (targets == 0) {
                error("targets")
                return@PlayerCommandExecutor
            }

            if (secondsPerPhase == 0L) {
                error("phase_time")
                return@PlayerCommandExecutor
            }

            if (phases == 0) {
                error("phases")
                return@PlayerCommandExecutor
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
        })
}