package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.utils.DataUtil
import kotlin.system.measureTimeMillis

object ItemhuntImport {

    val main: CommandAPICommand = CommandAPICommand("import")
        .withPermission(CommandPermission.OP)
        .executes(
            CommandExecutor { sender, args ->
                sender.sendMessage("Import target item datas(${measureTimeMillis { DataUtil.import() }}ms).")
            }
        )
}