package jp.iceserver.icecat

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractIceCat : JavaPlugin()
{
    /**
     * イベントリスナーを登録
     *
     * @param listeners Listenerを実装しているクラス
     */
    protected fun registerListeners(vararg listeners: Listener)
    {
        listeners.forEach { server.pluginManager.registerEvents(it, this) }
    }

    /**
     * コマンドを登録
     *
     * @param commands CommandExecutorを継承しているクラスとそのコマンド名前
     */
    protected fun registerCommands(vararg commands: Pair<String, CommandExecutor>)
    {
        commands.forEach { getCommand(it.first)?.setExecutor(it.second) }
    }

    /**
     * コマンドとタブ補完を登録
     *
     * @param commands CommandExecutorとTabCompleterを継承しているクラスとそのコマンド名前
     */
    protected fun registerCommandsAndCompleters(vararg commands: Pair<Pair<String, CommandExecutor>, TabCompleter>)
    {
        commands.forEach {
            registerCommands(it.first)
            getCommand(it.first.first)?.tabCompleter = it.second
        }
    }
}