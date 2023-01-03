package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import love.chihuyu.config.ConfigKeys
import love.chihuyu.game.GameManager
import love.chihuyu.game.TimePresets
import love.chihuyu.utils.EpochUtil

object ItemhuntStart {

    val main: CommandAPICommand = CommandAPICommand("start")
        .withArguments(
            StringArgument("時間").replaceSuggestions(
                ArgumentSuggestions.strings {
                    TimePresets.values().map { it.name }.toTypedArray()
                }
            )
        )
        .executesPlayer(
            PlayerCommandExecutor { sender, args ->
                val startedEpoch = EpochUtil.nowEpoch()
                val timePresets = try {
                    TimePresets.valueOf(args[0] as String)
                } catch (e: IllegalArgumentException) {
                    sender.sendMessage("$prefix 無効な時間指定です")
                    return@PlayerCommandExecutor
                }
                val materialCategories = plugin.config.getStringList(ConfigKeys.MATERIALS.key)!!
                val targets = plugin.config.getInt(ConfigKeys.TARGETS.key)
                val gameFinishEpoch = startedEpoch + timePresets.seconds
                val clearItem = plugin.config.getBoolean(ConfigKeys.CLEAR_ITEM.key)
                val nightVision = plugin.config.getBoolean(ConfigKeys.NIGHT_VISION.key)
                val keepInventory = plugin.config.getBoolean(ConfigKeys.KEEP_INVENTORY.key)
                val tpAfterStart = plugin.config.getBoolean(ConfigKeys.TP_AFTER_START.key)

                fun error(key: String) {
                    sender.sendMessage("$prefix ${key}が未設定です")
                    sender.sendMessage("$prefix /ih $key で設定を行ってください")
                }

                if (plugin.config.getList(ConfigKeys.MATERIALS.key) == null) {
                    error("materials")
                    return@PlayerCommandExecutor
                }

                if (plugin.config.getInt(ConfigKeys.TARGETS.key) == 0) {
                    error("targets")
                    return@PlayerCommandExecutor
                }

                if (plugin.config.getLong(ConfigKeys.PHASE_TIME.key) == 0L) {
                    error("phase_time")
                    return@PlayerCommandExecutor
                }

                if (plugin.config.getInt(ConfigKeys.PHASES.key) == 0) {
                    error("phases")
                    return@PlayerCommandExecutor
                }

                GameManager.startedEpoch = startedEpoch
                GameManager.phases = timePresets.seconds.toInt() / 300
                GameManager.secondsPerPhase = 300
                GameManager.materialCategories = materialCategories
                GameManager.targets = targets
                GameManager.gameFinishEpoch = gameFinishEpoch
                GameManager.clearItem = clearItem
                GameManager.nightVision = nightVision
                GameManager.keepInventory = keepInventory
                GameManager.tpAfterStart = tpAfterStart

                GameManager.prepare()
            }
        )
}
