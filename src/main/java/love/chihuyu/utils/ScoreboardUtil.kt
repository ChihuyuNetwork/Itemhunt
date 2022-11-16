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
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.RenderType

object ScoreboardUtil {

    fun updateServerScoreboard() {
        val board = plugin.server.scoreboardManager!!.mainScoreboard
        board.objectives.forEach(Objective::unregister)

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

        TargetItem.activeTarget.forEachIndexed { index, material ->
            val craftItemStack = CraftItemStack.asNMSCopy(ItemStack(material!!))
            val jpPlayer =
                plugin.server.onlinePlayers.firstOrNull { it.locale == "ja_jp" } ?: plugin.server.onlinePlayers.random()
            val translated =
                translator.getTranslationFor(jpPlayer, TranslationKey.of(craftItemStack.p())).colour().first()
            val point = TargetDataUtil.getPoint(material)
            scores.add(index + 2, "・$translated ${ChatColor.GRAY}(${point}pt)${ChatColor.RESET}")
        }

        scores.forEachIndexed { index, s ->
            objTarget.getScore(s).score = scores.lastIndex - index

            plugin.server.onlinePlayers.forEach { player ->
                objRanking.getScore(player).score = PlayerData.data[player.uniqueId]?.values?.sum() ?: 0
                player.scoreboard = board
            }
        }
    }
}