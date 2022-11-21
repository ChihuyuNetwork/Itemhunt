package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor

object CommandSuicide {

    val main = CommandAPICommand("suicide")
        .executesPlayer(PlayerCommandExecutor { sender, args ->
            sender.sendMessage("ouch!")
            sender.health = .0
        })
}