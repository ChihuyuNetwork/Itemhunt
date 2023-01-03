package love.chihuyu.game

import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.event.HoverEventSource
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameRule
import org.bukkit.Sound
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

object GameManager {

    fun sortedPhasesData() = PlayerData.wonPhases.toList()
        .sortedBy { it.second }
    fun sortedPointsData() = PlayerData.points.toList()
        .sortedByDescending { it.second.values.sum() }
        .filterNot { it.second.values.sum() == 0 }

    val board = plugin.server.scoreboardManager.mainScoreboard

    var started: Boolean = false

    var startedEpoch = 0L
    var phases = 0
    var secondsPerPhase = 0L
    var materialCategories = mutableListOf<String>()
    var targets = 0
    var gameFinishEpoch = 0L
    var clearItem = false
    var nightVision = false
    var keepInventory = false
    var tpAfterStart = false

    fun prepare(
        materialCategories: MutableList<String>,
        targets: Int,
        clearItem: Boolean,
        nightVision: Boolean,
        keepInventory: Boolean,
        tpAfterStart: Boolean,
        timePresets: TimePresets
    ) {
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
                it.playSound(it, Sound.UI_BUTTON_CLICK, 1f, 1f)
            }

            remainCountdown--
        }

        val taskStartGame = plugin.runTaskLater(5 * 20) {
            taskCountdown.cancel()

            this@GameManager.startedEpoch = EpochUtil.nowEpoch()
            this@GameManager.phases = timePresets.seconds.toInt() / 300
            this@GameManager.secondsPerPhase = 300
            this@GameManager.materialCategories = materialCategories
            this@GameManager.targets = targets
            this@GameManager.gameFinishEpoch = EpochUtil.nowEpoch() + timePresets.seconds
            this@GameManager.clearItem = clearItem
            this@GameManager.nightVision = nightVision
            this@GameManager.keepInventory = keepInventory
            this@GameManager.tpAfterStart = tpAfterStart

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

        val taskUpdateActiveItem = plugin.runTaskTimer(0, secondsPerPhase * 20) {
            PhaseData.elapsedPhases++

            TargetItem.activeTarget.clear()
            repeat(targets) { TargetItem.activeTarget += TargetItem.data.filterKeys { materialCategories.contains(it) }.values.flatMap { it.keys }.random() }
            ScoreboardUtil.updateServerScoreboard()

            val sortedMaterialPoints = PlayerData.points.toList().sortedByDescending { it.second.toList().sumOf { pair -> pair.second } }

            plugin.server.onlinePlayers.forEach { player ->
                PlayerData.wonPhases[player.uniqueId] = (PlayerData.wonPhases[player.uniqueId] ?: 0) +
                        (sortedMaterialPoints.indexOfFirst { it.first == player.uniqueId })

                player.sendMessage("フェーズ${PhaseData.elapsedPhases}開始！")
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            }
        }

        val taskTickGame = plugin.runTaskTimer(0, 20) {
            BossbarUtil.updateBossbar(startedEpoch, secondsPerPhase, phases)

            plugin.server.onlinePlayers.forEach { player ->
                player.sendActionBar(Component.text("現在の順位: ${PlayerData.wonPhases.keys.indexOf(player.uniqueId).inc()}位"))
            }
        }

        val taskGameEnd = plugin.runTaskLater((gameFinishEpoch - startedEpoch) * 20) {
            taskTickGame.cancel()
            taskUpdateActiveItem.cancel()

            BossbarUtil.removeBossbar("bruh")
            PhaseData.elapsedPhases = 0
            started = false

            plugin.server.onlinePlayers.forEach { player ->
                val yourRank = sortedPhasesData().map { it.first }.indexOf(player.uniqueId).inc()
                val winner = if (sortedPhasesData().isNotEmpty()) {
                    "\n勝者は${ChatColor.BOLD}${Bukkit.getOfflinePlayer(sortedPhasesData()[0].first).name}${ChatColor.RESET}です" +
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
                                    sortedPhasesData().joinToString("\n") {
                                        "#${sortedPhasesData().indexOf(it).inc()} " +
                                            "${Bukkit.getOfflinePlayer(it.first).name} " +
                                            "(${sortedPointsData().firstOrNull { p -> p.first == it.first }?.second?.values?.sum() ?: 0}pt)"
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