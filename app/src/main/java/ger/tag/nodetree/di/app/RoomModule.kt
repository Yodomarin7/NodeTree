package ger.tag.nodetree.di.app

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ger.tag.nodetree.AppDatabase
import ger.tag.nodetree.data.source.nodeLocal.INodeLocal
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideINodeLocal(db: AppDatabase): INodeLocal { return db.inodeLocal() }

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "myapp_database"
        ).build()
    }

}