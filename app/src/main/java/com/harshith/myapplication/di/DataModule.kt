package com.harshith.myapplication.di

import android.content.Context
import androidx.room.Room
import com.harshith.myapplication.data.DefaultTaskRepository
import com.harshith.myapplication.data.TaskRepository
import com.harshith.myapplication.data.source.local.TaskDao
import com.harshith.myapplication.data.source.local.TodoDataBase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: DefaultTaskRepository) : TaskRepository
}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class DataSourceModule{
//    @Singleton
//    @Binds
//    abstract fun bindNetworkDataRepository()
//}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TodoDataBase{
        return Room.databaseBuilder(
            context.applicationContext,
            TodoDataBase::class.java,
            "tasks.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(dataBase: TodoDataBase): TaskDao = dataBase.taskDao()
}