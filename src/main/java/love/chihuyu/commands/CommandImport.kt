package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.utils.DataUtil
import kotlin.system.measureTimeMillis

object CommandImport {

    val main: CommandAPICommand = CommandAPICommand("import")
        .executes(
            CommandExecutor { sender, args ->
                sender.sendMessage("Import target item datas(${measureTimeMillis { DataUtil.import() }}ms).")
            }
        )
}