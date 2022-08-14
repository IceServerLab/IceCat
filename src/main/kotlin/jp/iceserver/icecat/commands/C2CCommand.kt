package jp.iceserver.icecat.commands

import jp.iceserver.icecat.menus.c2c.C2CMainMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class C2CCommand : CommandExecutor
{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {
        sender as Player
        C2CMainMenu.INVENTORY.open(sender)
        return true
    }
}