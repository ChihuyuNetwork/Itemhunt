package love.chihuyu.game

import org.bukkit.Material

object TargetItem {

    val activeTarget: MutableSet<Material?> = mutableSetOf()
    val data = hashMapOf<String, MutableMap<Material, Int>>()
}
