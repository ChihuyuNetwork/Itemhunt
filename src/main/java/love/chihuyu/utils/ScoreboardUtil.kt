package love.chihuyu.utils

import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage
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

            if (!Itemhunt.started) {
                val scores = mutableListOf(
                    " ",
                    "待機中...",
                    "  "
                )

                scores.forEachIndexed { index, s ->
                    objTarget.getScore(s).score = scores.lastIndex - index
                }

                player.scoreboard = board
                return
            }

            val scores = mutableListOf(
                " ",
                "目標リスト",
                "  "
            )
            TargetItem.targetItem.forEachIndexed { index, material ->
                val craftItemStack = CraftItemStack.asNMSCopy(ItemStack(material!!))
                val translated = CraftChatMessage.fromComponent(craftItemStack.x())
                val point = TargetDataUtil.getPoint(material)
                scores.add(index + 2, "・$translated ${ChatColor.GRAY}(${point}pt)${ChatColor.RESET}")
            }

            scores.forEachIndexed { index, s ->
                objTarget.getScore(s).score = scores.lastIndex - index
            }

            plugin.server.onlinePlayers.forEach { p ->
                objRanking.getScore(p).score = PlayerData.data[p.uniqueId]?.values?.sum() ?: 0
            }

            player.scoreboard = board
        }
    }
}