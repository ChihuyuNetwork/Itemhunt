package love.chihuyu.utils

import love.chihuyu.Itemhunt
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object ItemUtil {

    fun create(
        material: Material,
        name: String? = null,
        amount: Int? = null,
        customModelData: Int? = null,
        lore: List<String>? = null,
        attributeModifier: Map<Attribute, AttributeModifier>? = null,
        enchantments: Map<Enchantment, Int>? = null,
        flags: List<ItemFlag>? = null,
        unbreakable: Boolean? = null,
    ): ItemStack {
        val item = ItemStack(material)
        if (amount != null) item.amount = amount

        val meta = item.itemMeta ?: return item
        if (name != null) meta.displayName(Component.text(name))
        if (unbreakable != null) meta.isUnbreakable = unbreakable
        if (lore != null) meta.lore(lore.map { Component.text(it) })
        if (customModelData != null) meta.setCustomModelData(customModelData)

        attributeModifier?.forEach { meta.addAttributeModifier(it.key, it.value) }
        enchantments?.forEach { item.addUnsafeEnchantment(it.key, it.value) }
        flags?.forEach { meta.addItemFlags(it) }

        item.itemMeta = meta
        return item
    }

    fun addPointHopperIfHavent(player: Player) {
        if (player.inventory.filterNotNull().none { item -> item.itemMeta?.hasCustomModelData() == true }) player.inventory.addItem(
            Itemhunt.POINT_HOPPER
        )
    }
}
