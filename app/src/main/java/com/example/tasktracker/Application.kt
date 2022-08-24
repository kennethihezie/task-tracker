package com.example.tasktracker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.example.tasktracker.di.taskModule
import com.example.tasktracker.model.migration
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {
    private val schemaVersion = 1L

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this) //init realmdb this covers all use of realm    within the project.

        val config: RealmConfiguration by lazy {
            RealmConfiguration.Builder()
                .schemaVersion(schemaVersion)
                .deleteRealmIfMigrationNeeded()
                .build()
        }

        Realm.setDefaultConfiguration(config)

        startKoin {
            androidLogger()
            androidContext(this@Application)
            koin.loadModules(listOf(taskModule))
        }
    }
}