package love.chihuyu.utils

import love.chihuyu.Itemhunt
import love.chihuyu.data.TargetCategory
import love.chihuyu.data.TargetItem
import org.bukkit.Material

object DataUtil {

    fun import() {
        val section = Itemhunt.plugin.config.getConfigurationSection("targets") ?: return
        section.getKeys(false).forEach { categoryName ->
            val category = section.getConfigurationSection(categoryName) ?: return
            category.getKeys(false).forEach { materialName ->
                val material = category.getInt(materialName)
                TargetItem.targetData[TargetCategory.valueOf(categoryName)]?.set(Material.valueOf(materialName), material)
            }
        }
    }
}
