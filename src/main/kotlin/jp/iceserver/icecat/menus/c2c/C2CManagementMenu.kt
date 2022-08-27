package jp.iceserver.icecat.menus.c2c

import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import dev.m1n1don.smartinvsr.inventory.content.SlotIterator
import jp.iceserver.icecat.IceCat
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class C2CManagementMenu : InventoryProvider
{
    companion object
    {
        val INVENTORY: SmartInventory = SmartInventory.builder()
            .id("c2c-management")
            .title("C2C >> 取引の管理")
            .size(5, 9)
            .provider(C2CManagementMenu())
            .manager(IceCat.plugin.invManager)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents)
    {
        for (i in 1..2)
        {
            contents.fillColumn(i * 4 - 1, ClickableItem.empty { item ->
                item.material = Material.WHITE_STAINED_GLASS_PANE
                item.displayName = " "
            })
        }

        val pagination = contents.pagination()
        val items = arrayOfNulls<ClickableItem>(0)

        /*
        for (slot in 0 until items.count())
        {
            items[slot] = ClickableItem.of( Bukkit.getOnlinePlayers().toList()[slot].getPlayerHead("${ChatColor.YELLOW}${Bukkit.getOnlinePlayers().toList()[slot].name}"))
            {
                val merchant = Bukkit.createMerchant(Component.text("${ChatColor.YELLOW}${Bukkit.getOnlinePlayers().toList()[slot].name}"))
                player.openMerchant(merchant, true)
            }
        }
         */

        pagination.setItems(*items)
        pagination.setItemsPerPage(35)
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.VERTICAL, 0, 0))

        contents.set(0, 8, ClickableItem.of({ i ->
            i.material = Material.EMERALD
            i.displayName = "${ChatColor.RESET}${ChatColor.GREEN}メニューに戻る"
        }, {
            C2CMainMenu.INVENTORY.open(player)
        }))

        contents.set(2, 8, ClickableItem.of({ i ->
            i.material = Material.ARROW
            i.displayName = "${ChatColor.BOLD}前のページ"
        }, {
            if (contents.pagination().isFirst) return@of
            INVENTORY.open(player, pagination.previous().page)
        }))

        contents.set(3, 8, ClickableItem.of({ i ->
            i.material = Material.ARROW
            i.displayName = "${ChatColor.BOLD}次のページ"
        }, {
            if (!contents.pagination().isLast) return@of
            INVENTORY.open(player, pagination.next().page)
        }))

        for (i in 0..3)
        {
            contents.fillColumn(if (i < 2) i else i + 2, ClickableItem.empty { item ->
                item.material = Material.BARRIER
                item.displayName = "${ChatColor.RED}未設定"
                item.lore = listOf(
                    " ",
                    "${ChatColor.YELLOW}左クリックで購入に必要なアイテムを選択できます。",
                    if (listOf(0, 2).contains(i)) "${ChatColor.GRAY}1個目のアイテム" else "${ChatColor.GRAY}2個目のアイテム"
                )
            })
        }
    }
}