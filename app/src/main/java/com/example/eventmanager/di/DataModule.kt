package com.example.eventmanager.di

import android.app.Application
import com.example.eventmanager.data.AppDatabase
import com.example.eventmanager.data.EventListDao
import com.example.eventmanager.data.EventListRepositoryImpl
import com.example.eventmanager.domain.EventListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindEventListRepository(impl: EventListRepositoryImpl): EventListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideEventListDao(
            application: Application
        ): EventListDao {
            return AppDatabase.getInstance(application).eventListDao()
        }
    }
}
