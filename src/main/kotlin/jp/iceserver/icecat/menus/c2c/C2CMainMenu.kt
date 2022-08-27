package jp.iceserver.icecat.menus.c2c

import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.c2c.C2CManager
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class C2CMainMenu : InventoryProvider
{
    companion object
    {
        val INVENTORY: SmartInventory = SmartInventory.builder()
            .id("c2c-main")
            .title("C2C >> メニュー")
            .size(3, 9)
            .provider(C2CMainMenu())
            .manager(IceCat.plugin.invManager)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents)
    {
        contents.fillColumn(7, ClickableItem.empty { item ->
            item.material = Material.WHITE_STAINED_GLASS_PANE
            item.displayName = " "
        })

        contents.set(1, 8, ClickableItem.of({ i ->
            i.material = Material.BARRIER
            i.displayName = "${ChatColor.LIGHT_PURPLE}閉じる"
        }, {
            INVENTORY.close(player)
        }))

        contents.set(1, 1, ClickableItem.of({ i ->
            i.material = Material.CHEST
            i.displayName = "${ChatColor.AQUA}マイ取引の管理"
        }, {
            if (!C2CManager.getInstance().hasProvider(player)) C2CManager.getInstance().addProvider(player)
            C2CManagementMenu.INVENTORY.open(player)
        }))

        contents.set(1, 3, ClickableItem.of({ i ->
            i.material = Material.ANVIL
            i.displayName = "${ChatColor.YELLOW}プロバイダーを検索"
        }, {
            TODO("AnvilGUIを開く")
        }))

        contents.set(1, 5, ClickableItem.of({ i ->
            i.material = Material.BOOK
            i.displayName = "${ChatColor.GREEN}取引一覧を表示"
        }, {
            C2CProvidersMenu.INVENTORY.open(player)
        }))
    }
}