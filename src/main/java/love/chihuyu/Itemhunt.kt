package love.chihuyu

import love.chihuyu.commands.CommandItemhunt
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot

class Itemhunt : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: JavaPlugin
        var started: Boolean = false
    }

    init {
        plugin = this
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        BossbarUtil.removeBossbar("bruh")
    }

    @EventHandler
    fun onPick(e: InventoryPickupItemEvent) {
        if (started) updateScoreboard()
    }

    @EventHandler
    fun onInventory(e: InventoryMoveItemEvent) {
        logger.info(started.toString())
        if (started) updateScoreboard()
    }

    private fun updateScoreboard() {
        server.onlinePlayers.forEach { player ->
            val board = server.scoreboardManager!!.newScoreboard
            board.objectives.forEach { it.unregister() }
            val obj = board.getObjective(DisplaySlot.SIDEBAR) ?:
            board.registerNewObjective("main", "", "   ${ChatColor.GOLD}${ChatColor.UNDERLINE}${ChatColor.BOLD}Item Hunt${ChatColor.RESET}   ")

            val sorted = PlayerData.data.mapValues { it.value[TargetItem.targetItem] }.toList().sortedBy { it.second }

            val scores = mutableListOf(
                " ",
                "#${sorted.indexOfFirst { it.first == player.uniqueId } + 1} ${player.name} / ${ChatColor.GREEN}${sorted.first { it.first == player.uniqueId }.second}${ChatColor.RESET}",
                "${ChatColor.STRIKETHROUGH}${ChatColor.BOLD}${ChatColor.GRAY}               "
            )

            sorted.forEachIndexed { index, pair ->
                if (index > 4) return@forEachIndexed
                scores.add(index, "#${index.inc()} ${Bukkit.getOfflinePlayer(pair.first).name} / ${ChatColor.GREEN}${pair.second}${ChatColor.RESET}")
            }

            scores.forEachIndexed { index, s ->
                obj.getScore(s).score = scores.lastIndex - index
            }

            player.scoreboard = board
        }
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

       CommandItemhunt.main.register()
    }
}