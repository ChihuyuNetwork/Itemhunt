package love.chihuyu.utils

import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.RenderType
import java.util.Locale

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

            val data = PlayerData.data.toList().sortedByDescending { it.second[TargetItem.targetItem] }
            val craftItemStack = CraftItemStack.asNMSCopy(ItemStack(TargetItem.targetItem!!))

            val scores = mutableListOf(
                " ",
                "目標: ${CraftChatMessage.fromComponent(craftItemStack.x())}",
                "  ",
                "   ",
                // #が先頭の時はRESETいれないと見えなくなる
                "${ChatColor.RESET}#${data.map { it.first }.indexOf(player.uniqueId) + 1} あなた / ${ChatColor.GREEN}${data.first { it.first == player.uniqueId }.second[TargetItem.targetItem] ?: 0}${ChatColor.RESET}pt",
                "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${" ".repeat(24)}"
            )

            data.forEachIndexed { index, pair ->
                if (index > 4) return@forEachIndexed
                val value = pair.second.toList().firstOrNull { it.first == TargetItem.targetItem }?.second ?: 0
                scores.add(index + 3, "${ChatColor.RESET}#${index.inc()} ${Bukkit.getOfflinePlayer(player.uniqueId).name} ${ChatColor.GREEN}${value}${ChatColor.RESET}pt")
            }

            scores.forEachIndexed { index, s ->
                obj.getScore(s).score = scores.lastIndex - index
            }

            player.scoreboard = board
        }
    }
}