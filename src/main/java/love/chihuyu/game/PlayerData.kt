package love.chihuyu.game

import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

object PlayerData {

    val points = mutableMapOf<UUID, MutableMap<Material, Int>>()
    val wonPhases = mutableMapOf<UUID, Int>()

    fun init(player: Player, phases: Int) {
        points[player.uniqueId] = mutableMapOf()
        repeat(phases) { points[player.uniqueId] = mutableMapOf() }
        this.wonPhases[player.uniqueId] = 0
    }
}