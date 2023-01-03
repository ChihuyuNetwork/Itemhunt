package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt.Companion.mainWorld
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import net.kyori.adventure.text.Component
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.util.*

object ItemhuntNewWorld {

    val main: CommandAPICommand = CommandAPICommand("createworld")
        .executes(
            CommandExecutor { sender, _ ->
                plugin.server.broadcast(Component.text("$prefix ワールドを再生成します"))

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
                plugin.server.broadcast(Component.text("$prefix 全てのワールドを再生成しました"))
            }
        )
}