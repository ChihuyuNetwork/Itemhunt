package love.chihuyu.data

import org.bukkit.Material

object TargetItem {

    val activeTarget: MutableSet<Material?> = mutableSetOf()

    // TODO:setting score per material
    val data = hashMapOf<String, MutableMap<Material, Int>>()
}