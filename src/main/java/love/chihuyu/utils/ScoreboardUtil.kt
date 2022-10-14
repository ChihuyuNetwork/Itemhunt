package love.chihuyu.utils

import love.chihuyu.Itemhunt
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.RenderType

object ScoreboardUtil {

    fun updateScoreboard() {
        plugin.server.onlinePlayers.forEach { player ->
            val board = plugin.server.scoreboardManager!!.newScoreboard
            val obj = board.registerNewObjective(
                "main",
                "",
                "   ${ChatColor.GOLD}${ChatColor.UNDERLINE}${ChatColor.BOLD}Item Hunt${ChatColor.RESET}   "
            )
            obj.displaySlot = DisplaySlot.SIDEBAR
            obj.renderType = RenderType.INTEGER

            if (Itemhunt.started) {
                val scores = mutableListOf(
                    " ",
                    "待機中...",
                    "  "
                )

                scores.forEachIndexed { index, s ->
                    obj.getScore(s).score = scores.lastIndex - index
                }

                player.scoreboard = board
                return
            }

            val data = PlayerData.data.toList().sortedByDescending { score -> score.second.map { it.value }.sum() }

            val scores = mutableListOf(
                " ",
                "目標リスト",
                "  ",
                "   ",
                // #が先頭の時はRESETいれないと見えなくなる
                "${ChatColor.RESET}#${data.map { it.first }.indexOf(player.uniqueId) + 1} ${ChatColor.BOLD}あなた${ChatColor.RESET} ${ChatColor.GREEN}${data.first { it.first == player.uniqueId }.second.values.sum()}${ChatColor.RESET}pt",
                "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(32)}"
            )

            TargetItem.targetItem.forEachIndexed { index, material ->
                val craftItemStack = CraftItemStack.asNMSCopy(ItemStack(material!!))
                scores.add(index + 2, "・${CraftChatMessage.fromComponent(craftItemStack.x())} ${ChatColor.GRAY}(${TargetItem.targetData.map { it.value }.first { it.containsKey(material) }[material]}pt)")
            }

            data.forEachIndexed { index, pair ->
                if (index > 4) return@forEachIndexed
                val value = pair.second.values.sum()
                scores.add(index + 3 + TargetItem.targetItem.size, "${ChatColor.RESET}#${index.inc()} ${Bukkit.getOfflinePlayer(pair.first).name} ${ChatColor.GREEN}${value}${ChatColor.RESET}pt")
            }

            scores.forEachIndexed { index, s ->
                obj.getScore(s).score = scores.lastIndex - index
            }

            player.scoreboard = board
        }
    }
}