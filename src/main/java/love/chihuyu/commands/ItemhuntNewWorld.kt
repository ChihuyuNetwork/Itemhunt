package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.WorldCreator
import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis


object ItemhuntNewWorld {

    val main: CommandAPICommand = CommandAPICommand("createworld")
        .executes(CommandExecutor { sender, args ->
            val seed = Random().nextLong()
            val worlds = plugin.server.worlds

            val regenerateWorld = measureTimeMillis {
                worlds.forEach { world ->
                    val oldFile = File(world.worldFolder, world.name)
                    plugin.server.unloadWorld(world, false)
                    if (!deleteDirectory(oldFile)) {
                        sender.sendMessage("$prefix ${world.name}の削除に失敗しました")
                    }

                    val newWorld = plugin.server.createWorld(
                        WorldCreator(world.name)
                            .environment(World.Environment.NORMAL)
                            .generateStructures(true)
                            .seed(seed)
                    )!!

                    plugin.server.onlinePlayers.forEach {
                        it.teleport(newWorld.spawnLocation)
                    }
                }
            }

            sender.sendMessage("$prefix 全てのワールドを再生成しました ${ChatColor.GRAY}(${regenerateWorld}ms)")
        })

    private fun deleteDirectory(path: File): Boolean {
        if (path.exists()) {
            val files = path.listFiles()
            files?.forEach { file2 ->
                if (file2.isDirectory) {
                    deleteDirectory(file2)
                } else {
                    file2.delete()
                }
            }
        }
        return path.delete()
    }
}