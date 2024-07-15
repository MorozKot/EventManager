package com.example.eventmanager.di

import android.app.Application
import com.example.eventmanager.presentation.EventItemFragment
import com.example.eventmanager.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: EventItemFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}