package love.chihuyu.utils

import love.chihuyu.Itemhunt
import love.chihuyu.data.TargetCategory
import love.chihuyu.data.TargetItem
import org.bukkit.Material

object TargetDataUtil {

    fun import() {
        val section = Itemhunt.plugin.config.getConfigurationSection("targets") ?: return
        section.getKeys(false).forEach { categoryName ->
            val category = section.getConfigurationSection(categoryName) ?: return
            category.getKeys(false).forEach { materialName ->
                val material = category.getInt(materialName)
                TargetItem.data.getOrPut(TargetCategory.valueOf(categoryName)) { mutableMapOf() }[Material.valueOf(materialName)] = material
            }
        }
    }

    fun getPoint(material: Material): Int? {
        return TargetItem.data.toList().firstOrNull { it.second.containsKey(material) }
            ?.second?.toList()?.firstOrNull { it.first == material }
            ?.second
    }
}