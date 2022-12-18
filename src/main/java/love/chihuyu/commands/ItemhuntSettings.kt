package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.ListArgumentBuilder
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import love.chihuyu.config.ConfigKeys
import love.chihuyu.data.TargetItem

object ItemhuntSettings {

    private val setMaterials: CommandAPICommand = CommandAPICommand("materials")
        .withArguments(
            ListArgumentBuilder<String>("出現する目標アイテム").allowDuplicates(false).withList(TargetItem.data.keys).withStringMapper().buildGreedy()
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as List<String>
                plugin.config.set(ConfigKeys.MATERIALS.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix 出現する目標アイテムを設定しました")
            }
        )

    private val getMaterials: CommandAPICommand = CommandAPICommand(ConfigKeys.MATERIALS.key)
        .executes(
            CommandExecutor { sender, _ ->
                val list = plugin.config.getList(ConfigKeys.MATERIALS.key)
                if (list == null) {
                    sender.sendMessage("$prefix 未設定の項目です")
                    return@CommandExecutor
                }
                sender.sendMessage("$prefix 出現する目標アイテムは以下の通りです\n" + list.joinToString("\n") { "・$it" })
            }
        )

    private val setTargets: CommandAPICommand = CommandAPICommand(ConfigKeys.TARGETS.key)
        .withArguments(
            IntegerArgument("1フェーズあたりの目標アイテムの数", 1)
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Int
                plugin.config.set(ConfigKeys.TARGETS.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix 出現する目標アイテムの数を設定しました")
            }
        )

    private val getTargets: CommandAPICommand = CommandAPICommand(ConfigKeys.TARGETS.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getInt(ConfigKeys.TARGETS.key)
                if (value == 0) {
                    sender.sendMessage("$prefix 未設定の項目です")
                    return@CommandExecutor
                }
                sender.sendMessage("$prefix 出現する目標アイテムの数は${value}です")
            }
        )

    private val setPhaseTime: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASE_TIME.key)
        .withArguments(
            LongArgument("1フェーズあたりの時間(秒)", 1)
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Long
                plugin.config.set(ConfigKeys.PHASE_TIME.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix フェーズの時間を設定しました")
            }
        )

    private val getPhaseTime: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASE_TIME.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getLong(ConfigKeys.PHASE_TIME.key)
                if (value == 0L) {
                    sender.sendMessage("$prefix 未設定の項目です")
                    return@CommandExecutor
                }
                sender.sendMessage("$prefix 1フェーズあたりの時間は${value}秒です")
            }
        )

    private val setPhases: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASES.key)
        .withArguments(
            IntegerArgument("フェーズ数", 1)
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Int
                plugin.config.set(ConfigKeys.PHASES.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix フェーズ数を設定しました")
            }
        )

    private val getPhases: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASES.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getInt(ConfigKeys.PHASES.key)
                if (value == 0) {
                    sender.sendMessage("$prefix 未設定の項目です")
                    return@CommandExecutor
                }
                sender.sendMessage("$prefix フェーズ数は${value}です")
            }
        )

    private val setPvp: CommandAPICommand = CommandAPICommand(ConfigKeys.PVP.key)
        .withArguments(
            BooleanArgument("PvP")
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Boolean
                plugin.config.set(ConfigKeys.PVP.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix PvPを設定しました")
            }
        )

    private val getPvp: CommandAPICommand = CommandAPICommand(ConfigKeys.PVP.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getBoolean(ConfigKeys.PVP.key)
                sender.sendMessage("$prefix PvPは${value}です")
            }
        )

    private val setNightVision: CommandAPICommand = CommandAPICommand(ConfigKeys.NIGHT_VISION.key)
        .withArguments(
            BooleanArgument("暗視効果")
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Boolean
                plugin.config.set(ConfigKeys.NIGHT_VISION.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix 暗視効果の有無を設定しました")
            }
        )

    private val getNightVision: CommandAPICommand = CommandAPICommand(ConfigKeys.NIGHT_VISION.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getBoolean(ConfigKeys.NIGHT_VISION.key)
                sender.sendMessage("$prefix 暗視効果は${value}です")
            }
        )

    private val setKeepInventory: CommandAPICommand = CommandAPICommand(ConfigKeys.KEEP_INVENTORY.key)
        .withArguments(
            BooleanArgument("インベントリ保持")
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Boolean
                plugin.config.set(ConfigKeys.KEEP_INVENTORY.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix インベントリ保持の有無を設定しました")
            }
        )

    private val getKeepInventory: CommandAPICommand = CommandAPICommand(ConfigKeys.KEEP_INVENTORY.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getBoolean(ConfigKeys.KEEP_INVENTORY.key)
                sender.sendMessage("$prefix インベントリ保持は${value}です")
            }
        )

    private val setTpAfterStart: CommandAPICommand = CommandAPICommand(ConfigKeys.TP_AFTER_START.key)
        .withArguments(
            BooleanArgument("開始後にTPするかどうか")
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Boolean
                plugin.config.set(ConfigKeys.TP_AFTER_START.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix 開始後にTPするかどうかを設定しました")
            }
        )

    private val getTpAfterStart: CommandAPICommand = CommandAPICommand(ConfigKeys.TP_AFTER_START.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getBoolean(ConfigKeys.TP_AFTER_START.key)
                sender.sendMessage("$prefix インベントリ保持は${value}です")
            }
        )

    private val setClearItem: CommandAPICommand = CommandAPICommand(ConfigKeys.CLEAR_ITEM.key)
        .withArguments(
            BooleanArgument("ゲームが終了したらアイテムを消去するかどうか")
        )
        .executes(
            CommandExecutor { sender, args ->
                val value = args[0] as Boolean
                plugin.config.set(ConfigKeys.CLEAR_ITEM.key, value)
                plugin.saveConfig()
                sender.sendMessage("$prefix アイテム消去を設定しました")
            }
        )

    private val getClearItem: CommandAPICommand = CommandAPICommand(ConfigKeys.CLEAR_ITEM.key)
        .executes(
            CommandExecutor { sender, _ ->
                val value = plugin.config.getBoolean(ConfigKeys.TP_AFTER_START.key)
                sender.sendMessage("$prefix アイテム消去は${value}です")
            }
        )

    val main: CommandAPICommand = CommandAPICommand("settings")
        .withSubcommands(
            setMaterials,
            getMaterials,
            setTargets,
            getTargets,
            setPhaseTime,
            getPhaseTime,
            setPhases,
            getPhases,
            setPvp,
            getPvp,
            setNightVision,
            getNightVision,
            setKeepInventory,
            getKeepInventory,
            setTpAfterStart,
            getTpAfterStart,
            setClearItem,
            getClearItem
        )
}
