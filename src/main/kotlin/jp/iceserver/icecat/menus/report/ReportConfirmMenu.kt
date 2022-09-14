package jp.iceserver.icecat.menus.report

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import dev.m1n1don.smartinvsr.inventory.ClickableItem
import dev.m1n1don.smartinvsr.inventory.SmartInventory
import dev.m1n1don.smartinvsr.inventory.content.InventoryContents
import dev.m1n1don.smartinvsr.inventory.content.InventoryProvider
import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.text.SimpleDateFormat
import java.time.Instant

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
            for (size in 0 until reasons.size - (if (reasons.contains("その他")) 2 else 0)) builder.append("${reasons[size].replace("§r", "")}, ")
            builder.setLength(builder.length - 2)

            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 30f, 1f)
            player.msg("&2>&a> レポートを送信しました！\n&7(対象者: ${target.name}, 理由: ${builder})")

            val client = WebhookClient.withUrl("https://discord.com/api/webhooks/1019605403497148477/XeIlBu2DL5x7Z_cH2knWLQUOv7B6UgzhAK9BkUSzzK7f7J6f1pDtXXObJ1lRCsClWAVv")

            val embedBuilder = WebhookEmbedBuilder()
                .setTitle(WebhookEmbed.EmbedTitle("レポートが届きました。", ""))
                .addField(WebhookEmbed.EmbedField(true, "報告者", player.name))
                .addField(WebhookEmbed.EmbedField(true, "対象者", target.name.toString()))
                .addField(WebhookEmbed.EmbedField(false, "座標", "${player.location.x} ${player.location.y} ${player.location.z}"))
                .addField(WebhookEmbed.EmbedField(false, "理由", builder.toString()))
                .addField(WebhookEmbed.EmbedField(false, "その他", if (reasons.contains("その他")) reasons[reasons.size - 1] else "なし"))
                .setColor(0xa047ff)
                .setThumbnailUrl("https://cravatar.eu/avatar/${target.name}/100")
                .setTimestamp(Instant.ofEpochMilli(System.currentTimeMillis()))

            val messageBuilder = WebhookMessageBuilder()
                .setUsername("IceServer Report")
                .setAvatarUrl("https://images-ext-2.discordapp.net/external/q35szkJdGNEokxVLNou1mnKqbFUIquR2O5xNBN3seu4/https/cdn.discordapp.com/icons/680934020992729090/7e7ba969e7f047b66caaf66493f0b3b3.png")
                .addEmbeds(embedBuilder.build())

            client.send(messageBuilder.build())

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