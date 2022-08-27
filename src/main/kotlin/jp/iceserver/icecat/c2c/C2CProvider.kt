package jp.iceserver.icecat.c2c

import org.bukkit.OfflinePlayer

data class C2CProvider(val id: Int, val player: OfflinePlayer, val transactions: List<C2CTransaction>)