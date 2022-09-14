package jp.iceserver.icecat.config

import hazae41.minecraft.kutils.bukkit.PluginConfigFile

object MainConfig : PluginConfigFile("config")
{
    var prefix by string("prefix")
    var reportContents by stringList("reportContents")
}