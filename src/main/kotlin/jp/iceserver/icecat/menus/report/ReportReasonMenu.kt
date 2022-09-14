package jp.iceserver.icecat.menus.report

import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import dev.m1n1don.smartinvsr.inventory.content.SlotIterator
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.config.MainConfig
import jp.iceserver.icecat.utils.getSlotPos
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

class ReportReasonMenu(private val target: OfflinePlayer) : InventoryProvider
{
    companion object
    {
        fun inventory(target: OfflinePlayer): SmartInventory = SmartInventory.builder()
            .id("report-reason")
            .title("${ChatColor.DARK_RED}${ChatColor.BOLD}Report${ChatColor.DARK_GRAY}｜${ChatColor.DARK_GREEN}${ChatColor.BOLD}レポート理由の選択")
            .size(3, 9)
            .provider(ReportReasonMenu(target))
            .manager(IceCat.plugin.invManager)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents)
    {
        val selectedList = mutableListOf<Int>()

        contents.fillColumn(7, ClickableItem.empty { item ->
            item.material = Material.WHITE_STAINED_GLASS_PANE
            item.displayName = " "
        })

        contents.set(1, 8, ClickableItem.of({ i ->
            i.material = Material.ARROW
            i.displayName = "${ChatColor.YELLOW}${ChatColor.BOLD}次に進む"
        }, {
            if (selectedList.size == 0)
            {
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                return@of
            }

            val reasons = mutableListOf<String>()
            selectedList.forEach {
                reasons.add("${ChatColor.RESET}${contents.get(it.getSlotPos().first, it.getSlotPos().second).get().displayName}".replace("§r", ""))
            }

            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 1f)

            if (reasons.contains("その他"))
            {
                AnvilGUI.Builder()
                    .title("「その他」の内容")
                    .text("ここに入力")
                    .onComplete { _, text ->
                        reasons.remove("その他")
                        reasons.add("その他")
                        reasons.add(text)
                        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 1f)
                        ReportConfirmMenu.inventory(target, reasons).open(player)
                        AnvilGUI.Response.close()
                    }
                    .plugin(IceCat.plugin)
                    .open(player)
                return@of
            }

            ReportConfirmMenu.inventory(target, reasons).open(player)
        }))

        val pagination = contents.pagination()
        val items = arrayOfNulls<ClickableItem>(MainConfig.reportContents.size)

        for (slot in 0 until items.count())
        {
            items[slot] = ClickableItem.of({ i ->
                i.material = Material.PAPER
                i.displayName = MainConfig.reportContents[slot]
            }, { e ->
                val selectedItem = e.currentItem
                val itemMeta = selectedItem?.itemMeta

                if (selectedList.contains(e.slot))
                {
                    itemMeta?.removeEnchant(Enchantment.LUCK)
                    itemMeta?.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                    selectedList.remove(e.slot)
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                }
                else
                {
                    itemMeta?.addEnchant(Enchantment.LUCK, 10, true)
                    itemMeta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    selectedList.add(e.slot)
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 30f, 1f)
                }

                selectedItem?.itemMeta = itemMeta
                e.currentItem = selectedItem
            })
        }

        pagination.setItems(*items)
        pagination.setItemsPerPage(21)
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.VERTICAL, 0, 0))
    }
}