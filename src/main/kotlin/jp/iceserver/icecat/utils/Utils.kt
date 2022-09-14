package jp.iceserver.icecat.utils

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import kotlin.math.floor

fun OfflinePlayer.getPlayerHead(): ItemStack
{
    val itemStack = ItemStack(Material.PLAYER_HEAD)
    val skull = itemStack.itemMeta as SkullMeta

    skull.displayName(Component.text("${ChatColor.GOLD}${this.name ?: "Unknown"}"))
    skull.owningPlayer = this
    itemStack.itemMeta = skull

    return itemStack
}

fun Player.setNickName(name: String)
{
    val coloredName = Component.text(ChatColor.translateAlternateColorCodes('&', name))
    this.displayName(coloredName)
    this.playerListName(coloredName)
}

fun Int.getSlotPos(): Pair<Int, Int>
    = Pair(floor(this.toDouble() / 9).toInt(), (this - floor(this.toDouble() / 9) * 9).toInt())