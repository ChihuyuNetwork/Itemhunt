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
                val timePresets = try {
                    TimePresets.valueOf(args[0] as String)
                } catch (e: IllegalArgumentException) {
                    sender.sendMessage("$prefix 無効な時間指定です")
                    return@PlayerCommandExecutor
                }

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

                GameManager.prepare(
                    plugin.config.getStringList(ConfigKeys.MATERIALS.key),
                    plugin.config.getInt(ConfigKeys.TARGETS.key),
                    plugin.config.getBoolean(ConfigKeys.CLEAR_ITEM.key),
                    plugin.config.getBoolean(ConfigKeys.NIGHT_VISION.key),
                    plugin.config.getBoolean(ConfigKeys.KEEP_INVENTORY.key),
                    plugin.config.getBoolean(ConfigKeys.TP_AFTER_START.key),
                    timePresets
                )
            }
        )
}