package jp.iceserver.icecat.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object PlayerData : Table()
{
    val id: Column<Int> = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)

    val uniqueId: Column<UUID> = uuid("uniqueId")

    val nickname: Column<String> = varchar("nickname", 16)
}