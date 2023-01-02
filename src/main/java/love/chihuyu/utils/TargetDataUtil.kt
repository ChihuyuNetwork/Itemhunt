package love.chihuyu.utils

import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.game.TargetItem
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object TargetDataUtil {

    fun import() {
        val yaml = YamlConfiguration()
        yaml.load(File("${plugin.dataFolder}/targets.yml"))
        val section = yaml.getConfigurationSection("targets") ?: return
        section.getKeys(false).forEach { categoryName ->
            val category = section.getConfigurationSection(categoryName) ?: return
            category.getKeys(false).forEach { materialName ->
                val material = category.getInt(materialName)
                TargetItem.data.getOrPut(categoryName) { mutableMapOf() }[Material.valueOf(materialName)] = material
            }
        }
    }

    fun getPoint(material: Material): Int? {
        return TargetItem.data.toList().firstOrNull { it.second.containsKey(material) }
            ?.second?.toList()?.firstOrNull { it.first == material }
            ?.second
    }
}