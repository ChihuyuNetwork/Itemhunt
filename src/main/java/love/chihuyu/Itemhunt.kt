package love.chihuyu

import love.chihuyu.commands.CommandItemhunt
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import love.chihuyu.utils.ScoreboardUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

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

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPick(e: EntityPickupItemEvent) {
        val item = e.item.itemStack
        val player = e.entity as? Player ?: return
        logger.info(item.toString())
        if (item.type != TargetItem.targetItem || e.item.itemStack.itemMeta?.hasCustomModelData() == true) return
        e.item.itemStack.itemMeta = e.item.itemStack.itemMeta.apply { this?.setCustomModelData(1) }
        updateStats(item, player)
    }

    private fun updateStats(stack: ItemStack, player: Player) {
        PlayerData.data[player.uniqueId]!![stack.type] = (PlayerData.data[player.uniqueId]!![stack.type] ?: 0) + stack.amount
        if (started) ScoreboardUtil.updateScoreboard()
        logger.info(PlayerData.data.toString())
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

       CommandItemhunt.main.register()
    }
}