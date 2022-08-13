package jp.iceserver.icecat

import net.milkbowl.vault.economy.Economy


class IceCat : AbstractIceCat()
{
    companion object
    {
        lateinit var plugin: IceCat
        lateinit var economy: Economy
    }

    override fun onEnable()
    {
        plugin = this

        if (!setupEconomy())
        {
            logger.severe(String.format("Vaultの依存関係が見つかりません！", description.name));
            server.pluginManager.disablePlugin(this);
            return;
        }
    }

    private fun setupEconomy(): Boolean
    {
        if (server.pluginManager.getPlugin("Vault") == null) return false

        val rsp = server.servicesManager.getRegistration(Economy::class.java) ?: return false
        economy = rsp.provider

        return true
    }
}