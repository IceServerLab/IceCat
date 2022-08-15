package jp.iceserver.icecat.conveters

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

object ItemStringConverter
{
    fun stringToItem(dataSt: String): ItemStack
    {
        val datas = dataSt.split("@@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val mt = Material.getMaterial(datas[0])!!
        var amount = 1
        var damage = 0
        try {
            if (datas.size >= 2) amount = datas[1].toInt()
            if (datas.size >= 3) damage = datas[2].toInt()
        } catch (ex: NumberFormatException) {
            ex.printStackTrace()
        }
        val enchants: MutableMap<Enchantment?, Int> = HashMap()
        if (datas.size >= 4 && !datas[3].equals("", ignoreCase = true)) {
            val datass = datas[3].split("&&".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var isValue = false
            var cash: Enchantment? = null
            for (data in datass) {
                if (!isValue) {
                    cash = Enchantment.getByKey(NamespacedKey.minecraft(data))
                    isValue = true
                } else {
                    val value = data.toInt()
                    enchants[cash] = value
                    isValue = false
                    cash = null
                }
            }
        }
        var itName = ""
        if (datas.size >= 5 && !datas[4].equals("", ignoreCase = true)) itName =
            datas[4].replace("_@_".toRegex(), "@").replace("_&_".toRegex(), "&")
        val lore: MutableList<String> = ArrayList()
        if (datas.size >= 6 && !datas[5].equals("", ignoreCase = true)) {
            val datass = datas[5].split("&&".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (data in datass) lore.add(data.replace("_@_".toRegex(), "@").replace("_&_".toRegex(), "&"))
        }
        val flags: MutableSet<ItemFlag> = HashSet()
        if (datas.size >= 7 && !datas[6].equals("", ignoreCase = true)) {
            val datass = datas[6].split("&&".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (data in datass) flags.add(ItemFlag.valueOf(data))
        }
        val item = ItemStack(mt, amount)
        val meta = item.itemMeta
        if (meta is Damageable) meta.damage = damage
        for (en in enchants.keys) {
            val data = enchants[en]!!
            meta.addEnchant(en!!, data, false)
        }
        if (!itName.equals("", ignoreCase = true)) meta.setDisplayName(itName)
        meta.lore = lore
        for (flag in flags) meta.addItemFlags(flag)
        item.itemMeta = meta
        return item
    }

    fun itemToString(item: ItemStack): String
    {
        var mtName = ""
        var amount = 0
        var damage = 0
        var enchants: Map<Enchantment, Int?> = HashMap()
        var itName = ""
        var lore: List<String> = ArrayList()
        var flags: Set<ItemFlag>? = null
        val meta = item.itemMeta
        val mt = item.type
        mtName = mt.name
        amount = item.amount
        if (meta != null) {
            if (meta is Damageable) damage = meta.damage
            if (meta.hasEnchants()) enchants = meta.enchants
            if (meta.hasDisplayName()) itName = meta.displayName
            if (meta.hasLore()) lore = meta.lore!!.toList()
            flags = meta.itemFlags
        }
        val resultSt = StringBuilder("$mtName@@$amount@@$damage@@")
        var first1 = true
        for (en in enchants.keys) {
            if (!first1) resultSt.append("&&") else first1 = false
            val data = enchants[en]!!
            resultSt.append(en.key.key).append("&&").append(data)
        }
        resultSt.append("@@").append(itName.replace("&".toRegex(), "_&_").replace("@".toRegex(), "_@_")).append("@@")
        if (lore != null) {
            var first2 = true
            for (lor in lore) {
                if (!first2) resultSt.append("&&") else first2 = false
                resultSt.append(lor.replace("&".toRegex(), "_&_").replace("@".toRegex(), "_@_"))
            }
        }
        resultSt.append("@@")
        if (flags != null) {
            var first3 = true
            for (flag in flags) {
                if (!first3) resultSt.append("&&") else first3 = false
                resultSt.append(flag.name)
            }
        }
        return resultSt.toString()
    }
}