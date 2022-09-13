package jp.iceserver.icecat.commands

import jp.iceserver.icecat.menus.report.ReportMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReportCommand : CommandExecutor
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        ReportMenu.INVENTORY.open(sender as Player)
        return true
    }
}