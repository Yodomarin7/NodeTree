package ger.tag.nodetree.ui.screen.node

import ger.tag.nodetree.data.source.nodeLocal.NodeEntity
import ger.tag.nodetree.ui.helper.BaseVMNav
import ger.tag.nodetree.ui.helper.BaseVMState

class NodeHelper {
    sealed class Page {
        class Wait(val txtR: Int? = null, val txt: String? = null): Page()
        class Error(val e: Throwable): Page()
        object Nodes: Page()
    }

    data class State(
        val p: Page? = null,

        val nodeEntity: NodeEntity = NodeEntity(0, "ROOT", null),
        val childes: List<NodeEntity> = listOf(),
        val enabledAction: Boolean = true
    ): BaseVMState

    sealed class Nav: BaseVMNav {
        class ToChild(val childId: Long): Nav()
        class ToParent(val parentId: Long): Nav()
        //object ToAddNode: Nav()
    }
}