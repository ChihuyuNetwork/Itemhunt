package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor

object CommandSuicide {

    val main: CommandAPICommand = CommandAPICommand("suicide")
        .executesPlayer(PlayerCommandExecutor { sender, _ ->
            sender.health = .0
        })
}