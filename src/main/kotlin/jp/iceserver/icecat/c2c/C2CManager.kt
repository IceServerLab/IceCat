package jp.iceserver.icecat.c2c

import jp.iceserver.icecat.conveters.ItemStringConverter
import jp.iceserver.icecat.tables.C2CProviders
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.Merchant
import org.bukkit.inventory.MerchantRecipe
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class C2CManager
{
    companion object
    {
        private var instance: C2CManager? = null

        fun getInstance(): C2CManager
        {
            if (instance == null)
                instance = C2CManager()

            return instance!!
        }
    }

    val providers: MutableMap<UUID, C2CProvider> = mutableMapOf()

    fun loadProviders()
    {
        transaction {
            C2CProviders.selectAll().forEach {
                val transactions: MutableList<C2CTransaction> = mutableListOf()
                it[C2CProviders.transactions].split(",").forEach { t ->
                    val list = t.split(">")
                    transactions.add(C2CTransaction(
                        ItemStringConverter.stringToItem(list[0]),
                        if (list.size == 3) ItemStringConverter.stringToItem(list[1]) else null,
                        ItemStringConverter.stringToItem(list[if (list.size == 3) 2 else 1]))
                    )
                }
                providers[it[C2CProviders.uniqueId]] = (C2CProvider(it[C2CProviders.id], Bukkit.getOfflinePlayer(it[C2CProviders.uniqueId]), transactions))
            }
        }
    }

    fun addProvider(target: Player)
    {
        transaction {
            val id = C2CProviders.insert {
                it[uniqueId] = target.uniqueId
                it[transactions] = String()
            }[C2CProviders.id]

            providers[target.uniqueId] = (C2CProvider(id, target, listOf()))
        }
    }

    fun hasProvider(target: Player): Boolean
    {
        providers.values.forEach { if (it.player.uniqueId == target.uniqueId) return true }
        return false
    }

    fun getMerchant(provider: OfflinePlayer): Merchant
    {
        val merchant: Merchant = Bukkit.createMerchant(Component.text("${ChatColor.YELLOW}${provider.name}"))

        val recipes = mutableListOf<MerchantRecipe>()
        providers[provider.uniqueId]!!.transactions.forEach {
            val recipe = MerchantRecipe(it.goods, 1)
            recipe.setExperienceReward(true)
            recipe.addIngredient(it.item1)
            if (it.item2 != null) recipe.addIngredient(it.item2)
            recipes.add(recipe)
        }

        merchant.recipes = recipes
        return merchant
    }
}