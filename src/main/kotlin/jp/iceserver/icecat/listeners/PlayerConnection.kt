package jp.iceserver.icecat.listeners

import jp.iceserver.icecat.tables.PlayerData
import jp.iceserver.icecat.utils.setNickName
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerConnection : Listener
{
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent)
    {
        val player = e.player
        if (!player.hasPermission("icecat.command.nickname")) return

        transaction {
            PlayerData.select { PlayerData.uniqueId eq player.uniqueId }.forEach {
                player.setNickName(it[PlayerData.nickname])
            }
        }
    }
}