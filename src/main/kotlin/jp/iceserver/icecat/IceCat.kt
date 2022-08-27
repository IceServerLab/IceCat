package jp.iceserver.icecat

import dev.m1n1don.smartinvsr.inventory.InventoryManager
import jp.iceserver.icecat.commands.*
import jp.iceserver.icecat.listeners.*
import jp.iceserver.icecat.tables.C2CProviders
import net.milkbowl.vault.economy.Economy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection

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

        val dbFolder = File(dataFolder, "/database")
        if (!dbFolder.exists()) dbFolder.mkdirs()

        val dbFile = File(dataFolder, "/database/icecat.db")
        if (!dbFile.exists()) dbFile.createNewFile()

        Database.connect("jdbc:sqlite:${dbFile.path}", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(
                C2CProviders
            )
        }

        registerListeners(
            ExplosionPrime()
        )

        registerCommands(
            "c2c" to C2CCommand(),
            "coi" to CoiCommand(),
            "spawn" to SpawnCommand(),
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