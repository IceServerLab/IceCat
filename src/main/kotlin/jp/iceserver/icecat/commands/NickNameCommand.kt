package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.utils.setNickName
import net.wesjd.anvilgui.AnvilGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class NickNameCommand : CommandExecutor
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        sender as Player

        if (!sender.hasPermission("icecat.command.nickname"))
        {
            sender.msg("&cあなたに実行する権限がありません。")
            return true
        }

        if (args.isEmpty())
        {
            AnvilGUI.Builder()
                .title("ニックネームを入力してください")
                .onComplete { _, it ->
                    sender.setNickName(it)
                    sender.msg("&eニックネームを&f「&r${it}&f」&eに変更しました。")
                    AnvilGUI.Response.close()
                }
                .open(sender)
        }

        sender.setNickName(args[0])
        sender.msg("&eニックネームを&f「&r${args[0]}&f」&eに変更しました。")
        return true
    }
}