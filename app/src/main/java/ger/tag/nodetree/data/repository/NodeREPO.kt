package ger.tag.nodetree.data.repository

import ger.tag.nodetree.data.helper.DoNotFindNode
import ger.tag.nodetree.data.source.nodeLocal.INodeLocal
import ger.tag.nodetree.data.source.nodeLocal.NodeEntity
import ger.tag.nodetree.data.source.nodeStore.NodeStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class NodeREPO(
    private val iNodeLocal: INodeLocal,
    private val nodeStore: NodeStore,
) {
    suspend fun getLastNodeId(): Long? {
        return nodeStore.getNodeId().firstOrNull()
    }

    suspend fun saveCurrentNodeId(nodeId: Long) {
        nodeStore.saveNodeId(nodeId)
    }

    suspend fun getNode(nodeId: Long): Flow<NodeEntity> {
        return flow {
            iNodeLocal.getNode(nodeId).collect { node->
                if(nodeId == 1L && node == null) {
                    iNodeLocal.insertNode(NodeEntity(1L, "ROOT", null))
                }
                else if(node == null) throw DoNotFindNode()
                else { emit(node) }
            }
        }
    }

    fun getChildes(nodeId: Long): Flow<List<NodeEntity>> {
        return iNodeLocal.getChildes(nodeId)
    }

    suspend fun addChild(nodeEntity: NodeEntity) {
        iNodeLocal.insertNode(nodeEntity)
    }

    suspend fun deleteNodeAndChildes(nodeEntity: NodeEntity) {
        iNodeLocal.deleteNode(nodeEntity)
    }
}






















