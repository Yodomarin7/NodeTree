package ger.tag.nodetree.data.source.nodeLocal

import androidx.room.*
import ger.tag.nodetree.data.source.nodeLocal.NodeArg.NODE_ID
import ger.tag.nodetree.data.source.nodeLocal.NodeArg.NODE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
abstract class INodeLocal {
    @Query("SELECT * FROM $NODE_TABLE")
    abstract fun getNodes(): Flow<List<NodeEntity>>

    @Query("SELECT * FROM $NODE_TABLE WHERE $NODE_ID=:nodeId")
    abstract fun getNode(nodeId: Long): Flow<NodeEntity?>

//    @Query("SELECT node_table.node_id, node_table.name, node_table.parent_id FROM $NODE_TABLE " +
//            "JOIN $CHILD_TABLE ON node_table.node_id=child_table.node_id " +
//            "WHERE child_table.node_id=:nodeId AND child_table.child_id=node_table.node_id")
//    abstract fun getChildes(nodeId: Long): Flow<List<NodeEntity>>
    @Query("SELECT * FROM $NODE_TABLE WHERE ${NodeArg.PARENT_ID}=:nodeId")
    abstract fun getChildes(nodeId: Long): Flow<List<NodeEntity>>

    @Query("SELECT * FROM $NODE_TABLE WHERE ${NodeArg.PARENT_ID}=:nodeId")
    abstract fun getChildesOnes(nodeId: Long): List<NodeEntity>

    @Delete
    abstract suspend fun deleteChildes(list: List<NodeEntity>)

    @Insert
    abstract suspend fun insertNode(nodeEntity: NodeEntity): Long

    @Update
    abstract suspend fun updateNode(nodeEntity: NodeEntity)

    @Transaction
    open suspend fun deleteNode(nodeEntity: NodeEntity) {
        val deletes = mutableListOf<NodeEntity>()
        deletes.add(nodeEntity)

        _getChildes(deletes, nodeEntity.nodeId)
        deleteChildes(deletes)
    }

    private fun _getChildes(deletes: MutableList<NodeEntity>, nodeId: Long) {
        val list = getChildesOnes(nodeId)
        deletes.addAll(list)
        list.forEach {
            _getChildes(deletes, it.nodeId)
        }
    }
}

























