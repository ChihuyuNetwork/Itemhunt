package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt.Companion.mainWorld
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.util.*

object ItemhuntNewWorld {

    val main: CommandAPICommand = CommandAPICommand("createworld")
        .executes(
            CommandExecutor { sender, _ ->
                sender.sendMessage("$prefix ワールドを再生成します")

                val seed = Random().nextLong()

                val newWorld = WorldCreator("world.$seed")
                    .environment(World.Environment.NORMAL)
                    .generateStructures(true)
                    .type(WorldType.NORMAL)
                    .seed(seed)
                    .createWorld()!!

                mainWorld = newWorld

                plugin.server.onlinePlayers.forEach {
                    it.teleport(mainWorld.spawnLocation)
                }

                plugin.server.unloadWorld(plugin.server.getWorld("world")!!, false)

                sender.sendMessage("$prefix 全てのワールドを再生成しました")
            }
        )
}
