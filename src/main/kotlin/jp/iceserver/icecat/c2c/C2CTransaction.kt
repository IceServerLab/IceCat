package jp.iceserver.icecat.c2c

import org.bukkit.inventory.ItemStack

data class C2CTransaction(val item1: ItemStack, val item2: ItemStack?, val goods: ItemStack)