package com.example.eventmanager.di

import androidx.lifecycle.ViewModel
import com.example.eventmanager.presentation.EventItemViewModel
import com.example.eventmanager.presentation.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventItemViewModel::class)
    fun bindEventItemViewModel(viewModel: EventItemViewModel): ViewModel
}
