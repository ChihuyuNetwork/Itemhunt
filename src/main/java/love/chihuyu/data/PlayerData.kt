package love.chihuyu.data

import org.bukkit.Material
import java.util.UUID

object PlayerData {

    val data = mutableMapOf<UUID, MutableMap<Material, Int>>()
}
