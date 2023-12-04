package com.e.companyapp.application

import android.app.Application
import com.e.companyapp.BuildConfig
import com.e.companyapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CompanyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CompanyApp)
            if (BuildConfig.BUILD_TYPE == "debug") {
                modules(
                    listOf(
                        application,
                        repositoryTest,
                        db,
                        mainScreen,
                        notification,
                        storeCompany
                    )
                )
            } else {
                modules(
                    listOf(
                        application,
                        repositoryProd,
                        db,
                        mainScreen,
                        notification,
                        storeCompany
                    )
                )
            }
        }
    }
}