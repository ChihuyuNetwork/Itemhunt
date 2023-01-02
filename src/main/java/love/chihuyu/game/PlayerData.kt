package love.chihuyu.game

import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

object PlayerData {

    val points = mutableMapOf<UUID, MutableList<MutableMap<Material, Int>>>()
    val wonPhases = mutableMapOf<UUID, Int>()

    fun init(player: Player, phases: Int) {
        points[player.uniqueId] = mutableListOf()
        repeat(phases) { points[player.uniqueId]?.add(mutableMapOf()) }
        this.wonPhases[player.uniqueId] = 0
    }
}