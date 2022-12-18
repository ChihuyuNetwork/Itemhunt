package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.utils.TargetDataUtil
import kotlin.system.measureTimeMillis

object ItemhuntImport {

    val main: CommandAPICommand = CommandAPICommand("import")
        .executes(
            CommandExecutor { sender, _ ->
                sender.sendMessage("Import target item datas(${measureTimeMillis { TargetDataUtil.import() }}ms).")
            }
        )
}
