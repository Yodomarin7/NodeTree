package ger.tag.nodetree.di.vm

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ger.tag.nodetree.data.repository.NodeREPO
import ger.tag.nodetree.data.source.nodeLocal.INodeLocal
import ger.tag.nodetree.data.source.nodeStore.NodeStore

@Module
@InstallIn(ViewModelComponent::class)
object NodeModule {

    @Provides
    fun provideNodeREPO(
        iNodeLocal: INodeLocal,
        nodeStore: NodeStore,
    ): NodeREPO { return NodeREPO(iNodeLocal, nodeStore) }

    @Provides
    fun provideNodeStore(@ApplicationContext appContext: Context): NodeStore { return NodeStore(appContext) }
}

































