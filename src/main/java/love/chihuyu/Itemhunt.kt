package love.chihuyu

import love.chihuyu.commands.CommandItemhunt
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import love.chihuyu.utils.DataUtil
import love.chihuyu.utils.ScoreboardUtil
import org.bukkit.GameMode
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Itemhunt : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: JavaPlugin
        var started: Boolean = false
        const val COUNTED_MODEL_DATA = 1
        val POINT_HOPPER = ItemStack(Material.NETHER_STAR).apply {
            val meta = this.itemMeta ?: return@apply
            this.itemMeta = meta

            meta.setDisplayName("ポイントホッパー")
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)

            this.addUnsafeEnchantment(Enchantment.MENDING, 0)
        }
    }

    init {
        plugin = this
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        BossbarUtil.removeBossbar("bruh")
        PlayerData.data.putIfAbsent(e.player.uniqueId, mutableMapOf())
        ScoreboardUtil.updateServerScoreboard()

        if (started) {
            player.gameMode = GameMode.SURVIVAL
            player.isInvulnerable = false
        } else {
            player.gameMode = GameMode.ADVENTURE
            player.isInvulnerable = true
        }
    }

    @EventHandler
    fun onHunger(e: FoodLevelChangeEvent) {
        e.isCancelled = true
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

    @EventHandler
    fun onClick(e: PlayerInteractEvent) {
        val action = e.action
        val player = e.player
        val item = e.item ?: return

        if (action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK && item != POINT_HOPPER) return
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMove(e: InventoryClickEvent) {
        val item = e.currentItem ?: return
        val player = e.whoClicked as Player
        val dest = e.clickedInventory ?: return

        if (dest.type != InventoryType.CHEST) return
    }

    private fun updateStats(stack: ItemStack, player: Player) {
        if (stack.itemMeta?.hasCustomModelData() == true || stack.type !in TargetItem.targetItem) return
        stack.itemMeta = stack.itemMeta?.apply {
            this.setCustomModelData(COUNTED_MODEL_DATA)
            this.lore = mutableListOf("§7§oThis item is already counted.")
        }
        val targetAndScore = mutableMapOf<Material, Int>()
        TargetItem.targetData.forEach { (_, materialAndScore) -> materialAndScore.forEach { targetAndScore[it.key] = it.value } }
        PlayerData.data[player.uniqueId]!![stack.type] = (PlayerData.data[player.uniqueId]!![stack.type] ?: 0) +
            (stack.amount * (targetAndScore[stack.type] ?: 1))
        if (started) ScoreboardUtil.updateServerScoreboard()
        logger.info(PlayerData.data.toString())
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

        CommandItemhunt.main.register()

        saveDefaultConfig()
        DataUtil.import()

        server.worlds.forEach {
            it.setGameRule(GameRule.FALL_DAMAGE, false)
            it.setGameRule(GameRule.KEEP_INVENTORY, false)
            it.setGameRule(GameRule.DROWNING_DAMAGE, false)
        }
    }
}
