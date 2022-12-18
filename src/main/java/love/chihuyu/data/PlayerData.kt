package love.chihuyu.data

import org.bukkit.Material
import java.util.*

object PlayerData {

    val points = mutableMapOf<UUID, MutableList<MutableMap<Material, Int>>>()
    val phases = mutableMapOf<UUID, Int>()
}