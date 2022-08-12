package jp.iceserver.icecat

class IceCat : AbstractIceCat()
{
    companion object
    {
        lateinit var plugin: IceCat
    }

    override fun onEnable()
    {
        plugin = this
    }
}