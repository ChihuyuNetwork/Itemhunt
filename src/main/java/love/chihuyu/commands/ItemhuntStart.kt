package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.ListArgumentBuilder
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.POINT_HOPPER
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.data.PhaseData
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetCategory
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.*
import me.pikamug.localelib.LocaleManager
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.*
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import java.time.Instant

object ItemhuntStart {

    val main = CommandAPICommand("start").withArguments(
        IntegerArgument("phases"),
        LongArgument("secondsPerPhase"),
        IntegerArgument("targets"),
        ListArgumentBuilder<TargetCategory>("materials").allowDuplicates(false)
            .withList(TargetCategory.values().toList()).withStringMapper().build()
    ).executes(
        CommandExecutor { sender, args ->
            val phases = args[0] as Int
            val secondsPerPhase = args[1] as Long
            val materials = mutableMapOf<Material, Int>()
            val targets = args[2] as Int
            val startedEpoch = nowEpoch()
            val gameFinishEpoch = startedEpoch + (secondsPerPhase * phases)

            (args[3] as List<TargetCategory>).forEach { category ->
                TargetItem.data[category]?.forEach { (material, score) ->
                    materials[material] = score
                }
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
                    TargetItem.targetItem += materials.keys.random()
                }
                ScoreboardUtil.updateServerScoreboard()

                plugin.server.onlinePlayers.forEach { player ->
                    player.sendMessage("フェーズ${PhaseData.elapsedPhases}開始！ / 目標アイテム一覧")

                    TargetItem.targetItem.forEachIndexed { _, material ->
                        val localeManager = LocaleManager()
                        val point = TargetDataUtil.getPoint(material!!)
                        localeManager.sendMessage(player, "・<item> ${ChatColor.GRAY}(${point}pt)", material, 0, mapOf())
                    }

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
            if (it.inventory.none { item -> item.itemMeta?.hasCustomModelData() == true }) it.inventory.addItem(POINT_HOPPER)
        }

        plugin.server.broadcastMessage(
            """
            ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
            ${" ".repeat(1)}
            ゲーム開始！
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
            player.spigot().sendMessage(
                TextComponent(
                    """
                    ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
                    ${" ".repeat(1)}
                    ${if (sortedPlayerData.isNotEmpty()) {
                        """
                    ゲーム終了！
                    勝者は${ChatColor.BOLD}${Bukkit.getOfflinePlayer(sortedPlayerData[0].first).name}${ChatColor.RESET}です
                    あなたは${sortedPlayerData.map { it.first }.indexOf(player.uniqueId).inc()}位でした
                    """
                    } else {
                        """
                    ゲーム終了！
                    勝者はいませんでした！
                    """
                    }}
                    ${" ".repeat(1)}
                    ${ChatColor.GOLD}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(42)}${ChatColor.RESET}
                    """.trimIndent()
                ).apply endMessage@{
                    this@endMessage.addExtra(
                        TextComponent("${ChatColor.UNDERLINE}ここにカーソルを合わせるとランキングが表示されます").apply rankingComponent@{
                            this@rankingComponent.hoverEvent = HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Text(
                                    sortedPlayerData.joinToString("\n") {
                                        "#${
                                        sortedPlayerData.indexOf(it).inc()
                                        } ${Bukkit.getOfflinePlayer(it.first).name} ${it.second.values.sum()}pt"
                                    }
                                )
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