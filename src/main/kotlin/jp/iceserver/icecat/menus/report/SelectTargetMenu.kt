package jp.iceserver.icecat.menus.report

import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import dev.m1n1don.smartinvsr.inventory.content.SlotIterator
import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.config.MainConfig
import jp.iceserver.icecat.utils.getPlayerHead
import net.kyori.adventure.text.Component
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta

class SelectTargetMenu : InventoryProvider
{
    companion object
    {
        val INVENTORY: SmartInventory = SmartInventory.builder()
            .id("report-selectTarget")
            .title("${ChatColor.DARK_RED}${ChatColor.BOLD}Report${ChatColor.DARK_GRAY}｜${ChatColor.DARK_GREEN}${ChatColor.BOLD}対象プレイヤーの選択")
            .size(4, 9)
            .provider(SelectTargetMenu())
            .manager(IceCat.plugin.invManager)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents)
    {
        var target: Player? = null

        contents.fillColumn(7, ClickableItem.empty { item ->
            item.material = Material.WHITE_STAINED_GLASS_PANE
            item.displayName = " "
        })

        contents.set(2, 8, ClickableItem.of({ i ->
            i.material = Material.ARROW
            i.displayName = "${ChatColor.YELLOW}${ChatColor.BOLD}次に進む"
        }, {
            if (target == null)
            {
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                return@of
            }

            if (target == player)
            {
                player.msg("${MainConfig.prefix} &c自分自身ををレポートすることはできません。")
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                return@of
            }

            player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 1f)
            ReportReasonMenu.inventory(target!!).open(player)
        }))

        contents.set(0, 8, ClickableItem.of({ i ->
            i.material = Material.ANVIL
            i.displayName = "${ChatColor.ITALIC}${ChatColor.BOLD}プレイヤーを検索"
        }, {
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 5f, 1f)
            AnvilGUI.Builder()
                .title("プレイヤーを検索")
                .text("ここに入力")
                .onComplete { _, text ->
                    val searchedPlayer = Bukkit.getOfflinePlayer(text)
                    if (!searchedPlayer.hasPlayedBefore())
                    {
                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                        player.msg("${MainConfig.prefix} &cプレイヤーが見つかりませんでした。")
                    }
                    else
                    {
                        if (searchedPlayer == player)
                        {
                            player.msg("${MainConfig.prefix} &c自分自身ををレポートすることはできません。")
                            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                            return@onComplete AnvilGUI.Response.close()
                        }
                        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 1f)
                        ReportReasonMenu.inventory(searchedPlayer).open(player)
                    }

                    AnvilGUI.Response.close()
                }
                .plugin(IceCat.plugin)
                .open(player)
        }))

        val pagination = contents.pagination()
        val items = arrayOfNulls<ClickableItem>(if (Bukkit.getOnlinePlayers().size > 28) 28 else Bukkit.getOnlinePlayers().size)

        val onlinePlayers = Bukkit.getOnlinePlayers().toList()
        for (slot in 0 until items.count())
        {
            items[slot] = ClickableItem.of(onlinePlayers[slot].getPlayerHead()) { e ->
                val selectedItem = e.currentItem
                val itemMeta = selectedItem?.itemMeta

                if (target != null && target == (itemMeta as SkullMeta).owningPlayer)
                {
                    itemMeta.displayName(Component.text("${ChatColor.WHITE}${target?.name}"))
                    target = null
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
                }
                else
                {
                    target = (itemMeta as SkullMeta).owningPlayer?.player
                    itemMeta.displayName(Component.text("${ChatColor.YELLOW}${target?.name}"))
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 30f, 1f)
                }

                selectedItem.itemMeta = itemMeta
                e.currentItem = selectedItem
            }
        }

        pagination.setItems(*items)
        pagination.setItemsPerPage(28)
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.VERTICAL, 0, 0))
    }
}