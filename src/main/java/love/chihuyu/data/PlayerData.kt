package love.chihuyu.data

import org.bukkit.Material
import org.bukkit.inventory.InventoryHolder
import java.util.UUID

object PlayerData {

    val data = mutableMapOf<UUID, MutableMap<Material, Int>>()
    val invData = mutableMapOf<UUID, InventoryHolder>()
}
