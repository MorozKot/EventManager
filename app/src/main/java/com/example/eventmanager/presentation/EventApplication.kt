package com.example.eventmanager.presentation

import android.app.Application
import com.example.eventmanager.di.DaggerApplicationComponent

class EventApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
