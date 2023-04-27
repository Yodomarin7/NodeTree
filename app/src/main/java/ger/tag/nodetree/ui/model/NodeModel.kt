package ger.tag.nodetree.ui.model

data class NodeModel(
    val id: Int,
    val name: String,
    val parent: NodeModel?,
    val childes: MutableList<NodeModel>
)