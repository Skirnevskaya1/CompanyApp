package com.e.companyapp.di

import androidx.room.Room
import com.example.utils.network.Network
import com.e.companyapp.interactor.MainInteractor
import com.e.companyapp.view.notification.StoreNotification
import com.e.companyapp.viewmodel.StoreCompany
import com.example.db.CompanyDB
import com.example.repository.IRepositoryLocal
import com.example.repository.IRepositoryRemote
import com.example.repository.localRepository.RoomRepository
import com.example.repository.localRepository.TestDataRepository
import com.example.repository.remoteRepository.api.RetrofitImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { com.example.utils.CoinImageLoader(androidContext()) }
    single { Network(androidContext()) }
}

val repositoryTest = module {
    single<IRepositoryRemote> { TestDataRepository() }
    single<IRepositoryLocal> { RoomRepository(get()) }
}

val repositoryProd = module {
    single<IRepositoryRemote> { RetrofitImpl() }
    single<IRepositoryLocal> { RoomRepository(get()) }
}

val mainScreen = module {
    factory { MainInteractor(get(), get()) }

    factory { StoreCompany(get(), get()) }
}

val db = module {
    single { Room.databaseBuilder(get(), CompanyDB::class.java, "CompanyDB").build() }
    single { get<CompanyDB>().handbooksDao() }
}

val notification = module {
    scope(named("NOTIFICATION_STORE")) {
        scoped { StoreNotification(get(), get()) }
    }
}

val storeCompany = module {
    scope(named("Company_App_STORE")) {
        scoped { StoreCompany(get(), get()) }
    }
}