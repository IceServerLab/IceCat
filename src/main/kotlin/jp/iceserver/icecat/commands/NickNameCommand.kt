package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.IceCat
import jp.iceserver.icecat.tables.PlayerData
import jp.iceserver.icecat.utils.setNickName
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class NickNameCommand : CommandExecutor
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        sender as Player

        if (args.isEmpty())
        {
            AnvilGUI.Builder()
                .title("ニックネームを変更する")
                .text("ここに入力")
                .itemLeft(ItemStack(Material.PAPER))
                .plugin(IceCat.plugin)
                .onComplete { _, it ->
                    setNickName(sender, it)
                    AnvilGUI.Response.close()
                }
                .open(sender)
            return true
        }

        setNickName(sender, args[0])
        return true
    }

    private fun setNickName(player: Player, name: String)
    {
        if (name.length >= 16)
        {
            player.msg("&cカラーコードを含め16文字以内でのみ使用できます。")
            return
        }

        transaction {
            PlayerData.insert {
                it[uniqueId] = player.uniqueId
                it[nickname] = name.replace("&k", "")
            }
        }

        player.setNickName(name.replace("&k", ""))
        player.msg("&eニックネームを&f「&r${name.replace("&k", "")}&f」&eに変更しました。")
    }
}