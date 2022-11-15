package love.chihuyu.utils

import com.convallyria.languagy.api.language.key.TranslationKey
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.started
import love.chihuyu.Itemhunt.Companion.translator
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.RenderType

object ScoreboardUtil {

    fun updateServerScoreboard() {
        plugin.server.onlinePlayers.forEach { player ->
            val board = plugin.server.scoreboardManager!!.newScoreboard
            val objTarget = board.registerNewObjective(
                "main",
                "",
                "   ${ChatColor.GOLD}${ChatColor.UNDERLINE}${ChatColor.BOLD}Item Hunt${ChatColor.RESET}   "
            ).apply {
                this.displaySlot = DisplaySlot.SIDEBAR
                this.renderType = RenderType.INTEGER
            }

            val objRanking = board.registerNewObjective(
                "ranking",
                ",",
                ""
            ).apply {
                this.displaySlot = DisplaySlot.PLAYER_LIST
            }

            if (!started) {
                objTarget.unregister()

                val objWaiting = board.registerNewObjective(
                    "main",
                    "",
                    "   ${ChatColor.GOLD}${ChatColor.UNDERLINE}${ChatColor.BOLD}Item Hunt${ChatColor.RESET}   ",
                    RenderType.INTEGER
                ).apply {
                    this.displaySlot = DisplaySlot.SIDEBAR
                }

                val scores = mutableListOf(
                    " ",
                    "待機中...",
                    "  "
                )

                scores.forEachIndexed { index, s ->
                    objWaiting.getScore(s).score = scores.lastIndex - index
                }

                objRanking.unregister()

                plugin.server.onlinePlayers.forEach { player -> player.scoreboard = board }
                return
            }

            val scores = mutableListOf(
                " ",
                "目標リスト",
                "  "
            )

            plugin.server.onlinePlayers.forEach { player1 ->
                scores.forEachIndexed { index, s ->
                    objTarget.getScore(s).score = scores.lastIndex - index
                    objRanking.getScore(player1).score = PlayerData.data[player1.uniqueId]?.values?.sum() ?: 0
                }
            }

            TargetItem.targetItem.forEachIndexed { index, material ->
                val craftItemStack = CraftItemStack.asNMSCopy(ItemStack(material!!))
                val translated =
                    translator.getTranslationFor(player, TranslationKey.of(craftItemStack.p())).colour().first()
                val point = TargetDataUtil.getPoint(material)
                scores.add(index + 2, "・$translated ${ChatColor.GRAY}(${point}pt)${ChatColor.RESET}")
            }

            player.scoreboard = board
        }
    }
}