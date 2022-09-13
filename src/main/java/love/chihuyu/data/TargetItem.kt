package love.chihuyu.data

import org.bukkit.Material

object TargetItem {

    var targetItem: Material? = null

    val targetData = mutableListOf<Material>(
        Material.DIRT,
        Material.DIRT_PATH,
        Material.COARSE_DIRT,
        Material.STONE,
        Material.COBBLESTONE,
        Material.SAND,
        Material.SANDSTONE,
        Material.COBBLESTONE_SLAB
    )
}