package love.chihuyu

import com.convallyria.languagy.api.language.Language
import com.convallyria.languagy.api.language.Translator
import love.chihuyu.commands.CommandItemhunt
import love.chihuyu.commands.CommandSuicide
import love.chihuyu.config.ConfigKeys
import love.chihuyu.game.GameManager
import love.chihuyu.game.GameManager.started
import love.chihuyu.game.PhaseData
import love.chihuyu.game.PlayerData
import love.chihuyu.game.TargetItem
import love.chihuyu.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.*
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.File

class Itemhunt : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: JavaPlugin
        lateinit var translator: Translator
        lateinit var mainWorld: World
        val prefix = "${ChatColor.GOLD}[IH]${ChatColor.RESET}"
        val POINT_HOPPER = ItemStack(Material.NETHER_STAR).apply {
            val meta = this.itemMeta ?: return@apply
            meta.displayName(Component.text("ポイント・ホッパー").style(Style.style(TextDecoration.BOLD)))
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
            meta.setCustomModelData(1)

            this.itemMeta = meta
            this.addUnsafeEnchantment(Enchantment.MENDING, 0)
        }
    }

    init {
        plugin = this
    }

    override fun onEnable() {
        val lang = File("${plugin.dataFolder}/lang/")
        lang.mkdirs()

        saveResource("lang/ja_jp.yml", false)
        saveResource("targets.yml", false)

        translator = Translator.of(plugin, Language.JAPANESE).debug(true)
        server.pluginManager.registerEvents(this, this)

        CommandItemhunt.main.register()
        CommandSuicide.main.register()

        saveDefaultConfig()
        TargetDataUtil.import()

        mainWorld = plugin.server.getWorld("world")!!
    }

    override fun onDisable() {
        translator.close()
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (e.entity !is Player) return
        when (e.cause) {
            DamageCause.ENTITY_ATTACK -> {
                val e = e as EntityDamageByEntityEvent
                val from = e.damager

                e.isCancelled = from !is Player || !plugin.config.getBoolean(ConfigKeys.PVP.key)
            }
            else -> e.isCancelled = true
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        BossbarUtil.removeBossbar("bruh")
        PlayerData.init(player, GameManager.phases)
        ScoreboardUtil.updateServerScoreboard()

        ItemUtil.addPointHopperIfHavent(player)
        player.gameMode = if (started) GameMode.SURVIVAL else GameMode.ADVENTURE
        player.isInvulnerable = false
        if (!started) player.activePotionEffects.forEach { player.removePotionEffect(it.type) }
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

        player.openInventory(Bukkit.createInventory(null, 9, Component.text("ポイント・ホッパー")))
    }

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        val item = e.itemDrop.itemStack

        if (item.itemMeta?.hasCustomModelData() == true) e.isCancelled = true
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val clicked = e.inventory
        val player = e.whoClicked as Player

        if (clicked.size != 9) return

        plugin.runTaskLater(1) {
            clicked.contents.filterNotNull().forEach { clicked ->
                if (clicked.type !in TargetItem.activeTarget) return@forEach

                PlayerData.points[player.uniqueId]?.get(PhaseData.elapsedPhases.dec())
                    ?.set(
                        clicked.type,
                        (
                            PlayerData.points[player.uniqueId]?.getOrNull(PhaseData.elapsedPhases.dec())?.getOrPut(clicked.type) { 0 }
                                ?: 0
                            ) +
                            (TargetDataUtil.getPoint(clicked.type) ?: 0) * clicked.amount
                    )

                // remove item
                clicked.amount = 0
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .8f, 1f)
            }

            ScoreboardUtil.updateServerScoreboard()
        }
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        val player = e.player as Player
        val inv = e.inventory

        if (inv.size != 9 || inv.isEmpty || inv.type != InventoryType.CHEST) return

        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)

        player.inventory.addItem(*inv.contents.filterNotNull().toTypedArray()).forEach {
            player.world.dropItemNaturally(player.location, it.value)
        }
    }

    @EventHandler
    fun onRespawn(e: PlayerRespawnEvent) {
        val player = e.player
        ItemUtil.addPointHopperIfHavent(player)

        if (plugin.config.getBoolean("night_vision")) player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 0, false, false, true))
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        e.drops.removeIf { it.itemMeta?.customModelData == 1 }
    }

    @EventHandler
    fun onPreLogin(e: PlayerLoginEvent) {
        val player = e.player
        player.teleport(mainWorld.spawnLocation)
    }
}