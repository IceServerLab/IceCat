package jp.iceserver.icecat.menus.report

import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.config.MainConfig
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player

class ReportConfirmMenu(private val target: OfflinePlayer, private val reasons: List<String>) : InventoryProvider
{
    companion object
    {
        fun inventory(target: OfflinePlayer, reasons: List<String>): SmartInventory = SmartInventory.builder()
            .id("report-confirm")
            .title("${ChatColor.DARK_RED}${ChatColor.BOLD}Report${ChatColor.DARK_GRAY}｜${ChatColor.DARK_RED}${ChatColor.BOLD}本当にレポートしますか？")
            .size(3, 9)
            .provider(ReportConfirmMenu(target, reasons))
            .manager(IceCat.plugin.invManager)
            .closeable(false)
            .build()
    }

    override fun init(player: Player, contents: InventoryContents)
    {
        for (i in 0..2)
        {
            contents.fillColumn(i * 4, ClickableItem.empty { item ->
                item.material = Material.WHITE_STAINED_GLASS_PANE
                item.displayName = " "
            })
        }

        contents.set(1, 2, ClickableItem.of({ i ->
            i.material = Material.LIME_TERRACOTTA
            i.displayName = "${ChatColor.GREEN}はい"
        }, {
            val builder = StringBuilder()
            reasons.forEach { builder.append("${it.replace("§r", "")}, ") }
            builder.setLength(builder.length - 2)

            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 30f, 1f)
            player.msg("&2>&a> レポートを送信しました！\n&7(対象者: ${target.name}, 理由: ${builder})")
            inventory(player, listOf()).close(player)
        }))

        contents.set(1, 6, ClickableItem.of({ i ->
            i.material = Material.RED_TERRACOTTA
            i.displayName = "${ChatColor.RED}いいえ"
        }, {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 50f, 1f)
            player.msg("&4>&c> レポートをキャンセルしました。")
            inventory(player, listOf()).close(player)
        }))
    }
}