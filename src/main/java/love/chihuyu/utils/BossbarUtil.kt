package love.chihuyu.utils

import love.chihuyu.Itemhunt.Companion.plugin
import org.bukkit.NamespacedKey

object BossbarUtil {

    fun removeBossbar(key: String) {
        plugin.server.getBossBar(NamespacedKey.fromString(key)!!)?.removeAll()
    }
}
