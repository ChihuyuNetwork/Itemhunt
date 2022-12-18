package love.chihuyu.data

import org.bukkit.Material

object TargetItem {

    val activeTarget: MutableSet<Material?> = mutableSetOf()
    val data = hashMapOf<String, MutableMap<Material, Int>>()
}
