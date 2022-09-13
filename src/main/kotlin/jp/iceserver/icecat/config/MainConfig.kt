package jp.iceserver.icecat.config

import hazae41.minecraft.kutils.bukkit.PluginConfigFile

object MainConfig : PluginConfigFile("config")
{
    var reportContents by stringList("reportContents")
}