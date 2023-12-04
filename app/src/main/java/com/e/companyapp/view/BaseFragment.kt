package com.e.companyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.e.companyapp.viewmodel.StoreCompany
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

abstract class BaseFragment<T> : Fragment() {

    protected lateinit var storeCompany: StoreCompany
    private lateinit var storeScopeInstance: Scope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeScopeInstance = KoinJavaComponent.getKoin().getOrCreateScope(
            "storeScopeId", named("CompanyApp_STORE"))
        storeCompany = storeScopeInstance.get()
    }


}