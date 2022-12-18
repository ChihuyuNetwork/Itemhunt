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
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameRule
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ItemhuntStart {

    val main: CommandAPICommand = CommandAPICommand("start").executesPlayer(
        PlayerCommandExecutor { sender, _ ->
            val phases = plugin.config.getInt(ConfigKeys.PHASES.key)
            val secondsPerPhase = plugin.config.getLong(ConfigKeys.PHASE_TIME.key)
            val materialCategories = plugin.config.getList(ConfigKeys.MATERIALS.key)
            val targets = plugin.config.getInt(ConfigKeys.TARGETS.key)
            val nightVision = plugin.config.getBoolean(ConfigKeys.NIGHT_VISION.key)
            val keepInventory = plugin.config.getBoolean(ConfigKeys.KEEP_INVENTORY.key)
            val tpAfterStart = plugin.config.getBoolean(ConfigKeys.TP_AFTER_START.key)
            val clearItem = plugin.config.getBoolean(ConfigKeys.CLEAR_ITEM.key)
            val startedEpoch = EpochUtil.nowEpoch()
            val gameFinishEpoch = startedEpoch + (secondsPerPhase * phases)

            fun onGameStart() {
                plugin.server.onlinePlayers.forEach { player ->
                    PlayerData.internalPhases = phases
                    PlayerData.init(player)
                    ItemUtil.addPointHopperIfHavent(player)

                    if (tpAfterStart) player.teleport(sender.location)
                    if (nightVision) player.addPotionEffect(
                        PotionEffect(
                            PotionEffectType.NIGHT_VISION,
                            Int.MAX_VALUE,
                            0,
                            false,
                            false,
                            false
                        )
                    )
                }

                plugin.server.worlds.forEach { world ->
                    world.setGameRule(GameRule.KEEP_INVENTORY, keepInventory)
                }

                plugin.server.broadcast(
                    Component.text(
                        """
                        ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
                        ${" ".repeat(1)}
                        アイテムハント開始！
                        ${" ".repeat(2)}
                        ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
                        """.trimIndent()
                    )
                )
            }

            fun onGameEnd() {
                BossbarUtil.removeBossbar("bruh")
                PhaseData.elapsedPhases = 0
                Itemhunt.started = false

                val sortedPhasesData = PlayerData.phases.toList()
                    .sortedByDescending { it.second }
                    .filterNot { it.second == 0 }
                val sortedPointsData = PlayerData.points.toList()
                    .sortedByDescending { it.second.sumOf { phase -> phase.values.sum() } }
                    .filterNot { it.second.sumOf { phase -> phase.values.sum() } == 0 }

                plugin.server.onlinePlayers.forEach { player ->
                    val yourRank = sortedPhasesData.map { it.first }.indexOf(player.uniqueId).inc()
                    val winner = if (sortedPhasesData.isNotEmpty()) {
                        "\n勝者は${ChatColor.BOLD}${Bukkit.getOfflinePlayer(sortedPhasesData[0].first).name}${ChatColor.RESET}です" +
                            "\nあなたは${if (yourRank == 0) "圏外" else "${yourRank}位"}でした"
                    } else {
                        "\n勝者はいませんでした"
                    }

                    val resultMsg = "${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}" +
                        "\nアイテムハント終了！" +
                        "\n$winner" +
                        "\n${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}"

                    player.sendMessage(Component.text(resultMsg))

                    player.sendMessage(
                        Component.text("${ChatColor.UNDERLINE}ここにカーソルを合わせるとランキングが表示されます").hoverEvent(
                            HoverEventSource {
                                HoverEvent.showText(
                                    Component.text(
                                        sortedPhasesData.joinToString("\n") {
                                            "#${sortedPhasesData.indexOf(it).inc()}" +
                                                "${Bukkit.getOfflinePlayer(it.first).name}" +
                                                "${it.second}勝利" +
                                                "(${sortedPointsData.firstOrNull { p -> p.first == it.first }?.second?.sumOf { phase -> phase.values.sum() } ?: 0}pt)"
                                        }
                                    )
                                )
                            }
                        )
                    )

                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, .5f, 1f)
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

            if (materialCategories == null) {
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
                repeat(targets) { TargetItem.activeTarget += TargetItem.data.filterKeys { it in materialCategories }.values.flatMap { it.keys }.random() }
                ScoreboardUtil.updateServerScoreboard()

                val phaseWinner = PlayerData.points.toList().filter { it.second.flatMap { map -> map.values }.sum() != 0 }.maxBy { it.second.sumOf { phase -> phase.values.sum() } }
                PlayerData.phases[phaseWinner.first] = PlayerData.phases[phaseWinner.first]?.inc() ?: 1

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