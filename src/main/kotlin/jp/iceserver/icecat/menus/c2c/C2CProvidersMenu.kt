package jp.iceserver.icecat.menus.c2c

import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import dev.m1n1don.smartinvsr.inventory.content.SlotIterator
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.c2c.C2CManager
import jp.iceserver.icecat.c2c.C2CProvider
import jp.iceserver.icecat.utils.getPlayerHead
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class C2CProvidersMenu : InventoryProvider
{
    companion object
    {
        val INVENTORY: SmartInventory = SmartInventory.builder()
            .id("c2c-providers")
            .title("C2C >> 取引一覧")
            .size(5, 9)
            .provider(C2CProvidersMenu())
            .manager(IceCat.plugin.invManager)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents)
    {
        contents.fillColumn(7, ClickableItem.empty { item ->
            item.material = Material.WHITE_STAINED_GLASS_PANE
            item.displayName = " "
        })

        val pagination = contents.pagination()
        val items = arrayOfNulls<ClickableItem>(C2CManager.getInstance().providers.size)

        for (slot in 0 until items.count())
        {
            val provider: C2CProvider = C2CManager.getInstance().providers.values.toList()[slot]
            val target = provider.player
            items[slot] = ClickableItem.of(player.getPlayerHead("${ChatColor.YELLOW}${target.name}"))
            {
                player.openMerchant(C2CManager.getInstance().getMerchant(target), true)
            }
        }

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
    }
}