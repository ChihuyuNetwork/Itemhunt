package love.chihuyu

import love.chihuyu.commands.CommandItemhunt
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import love.chihuyu.utils.ScoreboardUtil
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Itemhunt : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: JavaPlugin
        var started: Boolean = false
        const val COUNTED_MODEL_DATA = 1
    }

    init {
        plugin = this
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        BossbarUtil.removeBossbar("bruh")
        PlayerData.data.putIfAbsent(e.player.uniqueId, mutableMapOf())
        ScoreboardUtil.updateScoreboard()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPick(e: EntityPickupItemEvent) {
        val item = e.item.itemStack
        val player = e.entity as? Player ?: return
        if (item.type != TargetItem.targetItem || item.itemMeta?.hasCustomModelData() == true) return
        item.itemMeta = item.itemMeta?.apply {
            this.setCustomModelData(COUNTED_MODEL_DATA)
            this.lore = mutableListOf("§7§oThis item is already counted.")
        }
        updateStats(item, player)
    }

//    @EventHandler(priority = EventPriority.HIGHEST)
//    fun onClose(e: InventoryCloseEvent) {
//        val player = e.player as Player
//        val inventory = e.player.inventory
//        inventory.contents.filterNotNull().filter { it.type == TargetItem.targetItem }.forEach {
//            if (it.itemMeta?.hasCustomModelData() == true) return@forEach
//            it.itemMeta = it.itemMeta?.apply {
//                this.setCustomModelData(COUNTED_MODEL_DATA)
//                this.lore = mutableListOf("§7§oThis item is already counted.")
//            }
//            updateStats(it, player)
//        }
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMove(e: InventoryClickEvent) {
        val item = e.cursor ?: return
        val player = e.whoClicked as Player
        if (item.itemMeta?.hasCustomModelData() == true || item.type != TargetItem.targetItem) return
        item.itemMeta = item.itemMeta?.apply {
            this.setCustomModelData(COUNTED_MODEL_DATA)
            this.lore = mutableListOf("§7§oThis item is already counted.")
            updateStats(item, player)
        }
    }

    private fun updateStats(stack: ItemStack, player: Player) {
        val targetAndScore = mutableMapOf<Material, Int>()
        TargetItem.targetData.forEach { (category, materialAndScore) -> materialAndScore.forEach { targetAndScore[it.key] = it.value } }
        PlayerData.data[player.uniqueId]!![stack.type] = (PlayerData.data[player.uniqueId]!![stack.type] ?: 0) +
                (stack.amount * (targetAndScore[stack.type] ?: 1) )
        if (started) ScoreboardUtil.updateScoreboard()
        logger.info(PlayerData.data.toString())
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

        CommandItemhunt.main.register()

        server.worlds.forEach { it.setGameRule(GameRule.FALL_DAMAGE, false) }
        server.worlds.forEach { it.setGameRule(GameRule.KEEP_INVENTORY, false) }
    }
}