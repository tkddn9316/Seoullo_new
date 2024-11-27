package com.app.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.app.data.source.UserDataSource
import com.app.data.source.UserDataSourceImpl
import com.app.data.db.SeoulloDatabase
import com.app.data.db.UserDao
import com.app.data.source.SettingDataSource
import com.app.data.source.SettingDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val TOKEN_PREF_FILE = "token"

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

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext applicationContext: Context): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        produceFile = { applicationContext.preferencesDataStoreFile(TOKEN_PREF_FILE) }
    )

    @Provides
    @Singleton
    fun provideSettingDataSourceImpl(dataStore: DataStore<Preferences>): SettingDataSource {
        return SettingDataSourceImpl(dataStore)
    }
}