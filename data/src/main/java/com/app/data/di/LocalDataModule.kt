package com.app.data.di

import android.content.Context
import androidx.room.Room
import com.app.data.source.UserDataSource
import com.app.data.source.UserDataSourceImpl
import com.app.data.db.SeoulloDatabase
import com.app.data.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SeoulloDatabase {
        return Room.databaseBuilder(
            context,
            SeoulloDatabase::class.java,
            "SeoulloDB.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(seoulloDatabase: SeoulloDatabase): UserDao = seoulloDatabase.userDao()

    @Provides
    @Singleton
    fun provideUserDataSource(userDao: UserDao): UserDataSource = UserDataSourceImpl(userDao)
}