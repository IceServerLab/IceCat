package jp.iceserver.icecat.utils

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

fun Player.getPlayerHead(name: String): ItemStack
{
    val itemStack = ItemStack(Material.PLAYER_HEAD)
    val skull = itemStack.itemMeta as SkullMeta

    skull.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)))
    skull.owningPlayer = this
    itemStack.itemMeta = skull

    return itemStack
}