package jp.iceserver.icecat.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ExplosionPrimeEvent

class ExplosionPrime : Listener
{
    @EventHandler
    fun onBlockBreak(e: ExplosionPrimeEvent)
    {
        e.isCancelled = true
    }
}