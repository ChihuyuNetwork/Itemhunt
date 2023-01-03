package love.chihuyu.game

import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameRule
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration
import kotlin.properties.Delegates

object GameManager {

    val board = plugin.server.scoreboardManager.mainScoreboard

    var started: Boolean = false

    var startedEpoch by Delegates.notNull<Long>()
    var phases by Delegates.notNull<Int>()
    var secondsPerPhase by Delegates.notNull<Long>()
    var materialCategories = mutableListOf<String>()
    var targets by Delegates.notNull<Int>()
    var gameFinishEpoch by Delegates.notNull<Long>()
    var clearItem by Delegates.notNull<Boolean>()
    var nightVision by Delegates.notNull<Boolean>()
    var keepInventory by Delegates.notNull<Boolean>()
    var tpAfterStart by Delegates.notNull<Boolean>()

    fun prepare() {
        var remainCountdown = 5

        val taskCountdown = plugin.runTaskTimer(0, 20) {
            plugin.server.onlinePlayers.forEach {
                it.showTitle(
                    Title.title(
                        Component.text("${ChatColor.BOLD}$remainCountdown"),
                        Component.empty(),
                        Title.Times.times(
                            Duration.ofSeconds(0), Duration.ofSeconds(2), Duration.ofSeconds(0)
                        )
                    )
                )
            }

            remainCountdown--
        }

        val taskStartGame = plugin.runTaskLater(5 * 20) {
            taskCountdown.cancel()

            plugin.server.onlinePlayers.forEach { player ->
                PlayerData.init(player, phases)
                ItemUtil.addPointHopperIfHavent(player)

                if (tpAfterStart) player.teleport(player.world.spawnLocation)
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

            start()
        }
    }

    fun start() {
        started = true

        val taskTickGame = plugin.runTaskTimer(0, 20) {
            BossbarUtil.updateBossbar(startedEpoch, secondsPerPhase, phases)
        }

        val taskUpdateActiveItem = plugin.runTaskTimer(0, secondsPerPhase * 20) {
            PhaseData.elapsedPhases++

            TargetItem.activeTarget.clear()
            repeat(targets) { TargetItem.activeTarget += TargetItem.data.filterKeys { materialCategories.contains(it) }.values.flatMap { it.keys }.random() }
            ScoreboardUtil.updateServerScoreboard()

            val sortedMaterialPoints = PlayerData.points.toList().sortedByDescending { it.second.sumOf { materialPoint -> materialPoint.values.sum() } }

            plugin.server.onlinePlayers.forEach { player ->
                PlayerData.wonPhases[player.uniqueId] =
                    (PlayerData.wonPhases[player.uniqueId] ?: 0) +
                    (sortedMaterialPoints.indexOfFirst { it.first == player.uniqueId })
                val evalBaseInts = listOf(1, 2, 3, 4, 5).map { it * (plugin.server.onlinePlayers.size / 5f) }
                var nearestPlayerTeamIndex: Float? = 0f
                evalBaseInts.forEach {
                    if (PlayerData.wonPhases[player.uniqueId]!!.inc().toFloat() in it..(evalBaseInts[evalBaseInts.indexOf(it).inc() % 5])) {
                        val before = PlayerData.wonPhases[player.uniqueId]!!.inc().toFloat().compareTo(it)
                        val after = PlayerData.wonPhases[player.uniqueId]!!.inc().toFloat().compareTo((evalBaseInts[evalBaseInts.indexOf(it).inc() % 5]))
                        nearestPlayerTeamIndex = if (before > after) {
                            evalBaseInts[evalBaseInts.indexOf(it).inc() % 5]
                        } else {
                            it
                        }
                    }
                    nearestPlayerTeamIndex = null
                }

                val team = board.getTeam(nearestPlayerTeamIndex.toString()) ?: board.registerNewTeam(nearestPlayerTeamIndex.toString())
                when (evalBaseInts.indexOf(nearestPlayerTeamIndex)) {
                    0 -> {
                        team.color(NamedTextColor.AQUA)
                    }
                    1 -> {
                        team.color(NamedTextColor.GREEN)
                    }
                    2 -> {
                        team.color(NamedTextColor.YELLOW)
                    }
                    3 -> {
                        team.color(NamedTextColor.GOLD)
                    }
                    4 -> {
                        team.color(NamedTextColor.RED)
                    }
                    else -> {
                        team.color(NamedTextColor.GRAY)
                    }
                }
                board.getPlayerTeam(player)?.removePlayer(player)
                team.addPlayer(player)

                player.sendMessage("フェーズ${PhaseData.elapsedPhases}開始！")
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            }
        }

        val taskGameEnd = plugin.runTaskLater((gameFinishEpoch - startedEpoch) * 20) {
            taskTickGame.cancel()
            taskUpdateActiveItem.cancel()

            BossbarUtil.removeBossbar("bruh")
            PhaseData.elapsedPhases = 0
            started = false

            val sortedPhasesData = PlayerData.wonPhases.toList()
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
    }
}
