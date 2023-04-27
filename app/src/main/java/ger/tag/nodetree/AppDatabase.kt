package ger.tag.nodetree

import androidx.room.Database
import androidx.room.RoomDatabase
import ger.tag.nodetree.data.source.nodeLocal.INodeLocal
import ger.tag.nodetree.data.source.nodeLocal.NodeEntity

@Database(
    version = 1,
    entities = [
        NodeEntity::class,
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun inodeLocal(): INodeLocal
}