package jp.iceserver.icecat.menus.report

import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import dev.m1n1don.smartinvsr.inventory.content.SlotIterator
import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.config.MainConfig
import jp.iceserver.icecat.utils.getSlotPos
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import kotlin.math.floor

@Suppress("DEPRECATION")
class ReportMenu : InventoryProvider
{
    companion object
    {
        val INVENTORY: SmartInventory = SmartInventory.builder()
            .id("report-main")
            .title("${ChatColor.DARK_RED}${ChatColor.BOLD}Report${ChatColor.DARK_GRAY}｜${ChatColor.DARK_GREEN}${ChatColor.BOLD}レポート内容の選択")
            .size(3, 9)
            .provider(ReportMenu())
            .manager(IceCat.plugin.invManager)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents)
    {
        val selectedList = mutableListOf<String>()

        contents.fillColumn(7, ClickableItem.empty { item ->
            item.material = Material.WHITE_STAINED_GLASS_PANE
            item.displayName = " "
        })

        contents.set(1, 8, ClickableItem.of({ i ->
            i.material = Material.ARROW
            i.displayName = "${ChatColor.YELLOW}${ChatColor.BOLD}次に進む"
        }, {
            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10f, 1f)
            INVENTORY.close(player)
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
                val name = "${ChatColor.RESET}${selectedItem?.itemMeta?.displayName}"

                if (selectedList.contains(name))
                {
                    itemMeta?.removeEnchant(Enchantment.LUCK)
                    itemMeta?.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
                    selectedList.remove(name)
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                }
                else
                {
                    itemMeta?.addEnchant(Enchantment.LUCK, 10, true)
                    itemMeta?.addItemFlags(ItemFlag.HIDE_ENCHANTS)
                    selectedList.add(name)
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