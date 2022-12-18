package love.chihuyu.data

import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

object PlayerData {

    val points = mutableMapOf<UUID, MutableList<MutableMap<Material, Int>>>()
    val phases = mutableMapOf<UUID, Int>()

    var internalPhases = 0

    fun init(player: Player) {
        points[player.uniqueId] = mutableListOf()
        repeat(internalPhases) { points[player.uniqueId]?.add(mutableMapOf()) }
        this.phases[player.uniqueId] = 0
    }
}
