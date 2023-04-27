package ger.tag.nodetree.data.source.nodeStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val DATA_STORE_NAME = "node_store"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class NodeStore(
    private val context: Context
) {
    companion object{
        val NODE_ID = longPreferencesKey("node_id")
    }

    suspend fun saveNodeId(nodeId: Long) {
        context.dataStore.edit { preferences->
            preferences[NODE_ID] = nodeId
        }
    }

    suspend fun getNodeId(): Flow<Long> = context.dataStore.data.map { preferences  ->
        preferences[NODE_ID] ?: 1L
    }
}