package jp.iceserver.icecat.commands

import hazae41.minecraft.kutils.bukkit.msg
import jp.iceserver.icecat.config.MainConfig
import jp.iceserver.icecat.menus.report.SelectTargetMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReportCommand : CommandExecutor
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        sender as Player
        if (sender.isGliding)
        {
            sender.msg("${MainConfig.prefix} &cエリトラでの飛行中はレポートできません。")
            return true
        }

        SelectTargetMenu.INVENTORY.open(sender)
        return true
    }
}