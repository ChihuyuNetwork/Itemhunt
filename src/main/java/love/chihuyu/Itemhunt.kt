package love.chihuyu

import love.chihuyu.commands.CommandItemhunt
import love.chihuyu.data.PlayerData
import love.chihuyu.data.TargetItem
import love.chihuyu.utils.BossbarUtil
import love.chihuyu.utils.DataUtil
import love.chihuyu.utils.ScoreboardUtil
import love.chihuyu.utils.runTaskLater
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDropItemEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Itemhunt : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: JavaPlugin
        var started: Boolean = false
        val POINT_HOPPER = ItemStack(Material.NETHER_STAR).apply {
            val meta = this.itemMeta ?: return@apply
            meta.setDisplayName("ポイントホッパー")
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)

            this.itemMeta = meta
            this.addUnsafeEnchantment(Enchantment.MENDING, 0)
        }
    }

    init {
        plugin = this
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

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        BossbarUtil.removeBossbar("bruh")
        PlayerData.data.putIfAbsent(e.player.uniqueId, mutableMapOf())
        ScoreboardUtil.updateServerScoreboard()

        if (started) {
            player.gameMode = GameMode.SURVIVAL
            player.isInvulnerable = false
            if (POINT_HOPPER !in player.inventory.contents) player.inventory.addItem(POINT_HOPPER)
        } else {
            player.gameMode = GameMode.ADVENTURE
            player.isInvulnerable = true
        }
    }

    @EventHandler
    fun onHunger(e: FoodLevelChangeEvent) {
        e.isCancelled = true
    }

    @EventHandler
    fun onClick(e: PlayerInteractEvent) {
        val action = e.action
        val player = e.player
        val item = e.item ?: return

        if ((action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) || item != POINT_HOPPER) return

        val holder = InventoryHolder { Bukkit.createInventory(null, InventoryType.DROPPER, "ポイント・ホッパー") }

        PlayerData.invData[player.uniqueId] = holder
        player.openInventory(holder.inventory)
    }

    @EventHandler
    fun onDrop(e: EntityDropItemEvent) {
        val item = e.itemDrop.itemStack

        if (item == POINT_HOPPER && e.entity is Player) e.isCancelled = true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val clicked = e.inventory
        val player = e.whoClicked as Player

        if (clicked.size != 9) return

        plugin.runTaskLater(1) {
            clicked.contents.filterNotNull().forEach {
                if (it.type !in TargetItem.targetItem) return@forEach

                PlayerData.data[player.uniqueId]?.set(
                    it.type,
                    (PlayerData.data[player.uniqueId]?.get(it.type) ?: 0) +
                            (DataUtil.getPoint(it.type) ?: 0) * it.amount
                )

                //remove item
                it.amount = 0
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .8f, 1f)
            }
        }

        ScoreboardUtil.updateServerScoreboard()
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val player = e.player as Player
        val inv = e.inventory

        if (inv.size == 9 && !inv.isEmpty)

        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)

        player.inventory.addItem(*inv.contents.filterNotNull().toTypedArray()).forEach {
            player.world.dropItemNaturally(player.location, it.value)
        }
    }
}