package ger.tag.nodetree.ui.screen.node

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ger.tag.nodetree.data.repository.NodeREPO
import ger.tag.nodetree.data.source.nodeLocal.NodeEntity
import ger.tag.nodetree.ui.activity.Navigation
import ger.tag.nodetree.ui.helper.BaseVM
import ger.tag.nodetree.ui.screen.node.NodeHelper.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class NodeVM @Inject constructor(
    savedState: SavedStateHandle,
    private val nodeREPO: NodeREPO
): BaseVM<State, Nav>() {
    private val nodeId: Long = checkNotNull(savedState.get(Navigation.Args.NODE_ID))
    private val exception = CoroutineExceptionHandler { _, e ->
        setState { copy(p = Page.Error(e = e), enabledAction = true) }
    }
    override fun initState() = State()
    init {
        if(nodeId == 1L) {
            viewModelScope.launch(exception) {
                val nodeId = nodeREPO.getLastNodeId() ?: 1L
                if(nodeId != 1L) setNav(Nav.ToChild(nodeId))
            }
        }
        getNode()
    }

    fun saveCurrentNodeId(nodeId: Long, nav: Nav) {
        viewModelScope.launch(exception) {
            nodeREPO.saveCurrentNodeId(nodeId)
            setNav(nav)
        }
    }

    var job: Job? = null
    fun getNode() {
        setState { copy(p = Page.Wait()) }
        job?.cancel()
        job = viewModelScope.launch(exception) {
            launch {
                nodeREPO.getNode(nodeId).collect {
                    setState { copy(p = Page.Nodes, nodeEntity = it) }
                }
            }
            launch {
                nodeREPO.getChildes(nodeId).collect {
                    setState { copy(childes = it) }
                }
            }
        }
    }

    private fun getHash(nodeName: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashArray = digest.digest(nodeName.toByteArray(StandardCharsets.UTF_8))
        val hexStr = StringBuilder()

        for(i in hashArray.size-20 until hashArray.size) {
            val hex = Integer.toHexString(0xff and hashArray.get(i).toInt())
            if(hex.length == 1) hexStr.append('0')
            hexStr.append(hex)
        }
        return hexStr.toString()
    }

    fun addChild() {
        setState { copy(enabledAction = false) }
        viewModelScope.launch(exception) {
            delay(250)
            val nodeHash = state.value.nodeEntity.hashCode() + state.value.childes.hashCode()
            val nodeEntity = NodeEntity(0L, getHash(nodeHash.toString()),
                state.value.nodeEntity.nodeId)
            nodeREPO.addChild(nodeEntity)
            setState { copy(enabledAction = true) }
        }
    }

    fun deleteNodeAndChildes(nodeEntity: NodeEntity) {
        setState { copy(enabledAction = false) }
        viewModelScope.launch(exception) {
            delay(250)
            nodeREPO.deleteNodeAndChildes(nodeEntity)
            setState { copy(enabledAction = true) }
        }
    }

}














