package jp.iceserver.icecat

import dev.m1n1don.smartinvsr.inventory.InventoryManager
import jp.iceserver.icecat.commands.*
import jp.iceserver.icecat.listeners.*
import net.milkbowl.vault.economy.Economy

class IceCat : AbstractIceCat()
{
    companion object
    {
        lateinit var plugin: IceCat
        lateinit var economy: Economy
    }

    val invManager: InventoryManager = InventoryManager(this)

    override fun onEnable()
    {
        plugin = this

        if (!setupEconomy())
        {
            logger.severe(String.format("Vaultの依存関係が見つかりません！", description.name));
            server.pluginManager.disablePlugin(this);
            return;
        }

        invManager.init()

        registerListeners(
            ExplosionPrime()
        )

        registerCommands(
            "coi" to CoiCommand(),
            "nickname" to NickNameCommand()
        )
    }

    private fun setupEconomy(): Boolean
    {
        if (server.pluginManager.getPlugin("Vault") == null) return false

        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        economy = rsp.provider

        return true
    }
}