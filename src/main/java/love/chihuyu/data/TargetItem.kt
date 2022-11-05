package love.chihuyu.data

import org.bukkit.Material

object TargetItem {

    var targetItem: MutableSet<Material?> = mutableSetOf()

    // TODO:setting score per material
    val targetData = hashMapOf<TargetCategory, MutableMap<Material, Int>>()
}