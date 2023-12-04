package com.e.companyapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.utils.network.Network
import com.e.companyapp.interactor.MainInteractor
import com.example.models.Company
import com.example.models.ConfigToRequest
import com.example.models.JsonFromServer
import com.example.models.UserSession
import com.example.utils.Assistant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.ArrayList

class StoreCompany(

    private val interactor: MainInteractor,
    private val network: Network,
    private var userSession: MutableLiveData<UserSession> = MutableLiveData(),
    private var departmentList: MutableLiveData<ArrayList<Company>> = MutableLiveData(),

) : BaseViewModel() {

    fun getUserSession() = userSession
    fun getDepartmentList() = departmentList

    fun initLogin(login: String, pwd: String, typeAuthorization: Boolean) {
        Log.println(Log.INFO, "storeCompanyApp.getLogin", "Запрос авторизации на сервере")
        Log.println(
            Log.INFO,
            "storeCompanyApp.getLogin",
            "login: $login pwd: $pwd typeAuthorization: $typeAuthorization"
        )

        val payload = UserAutorizationActionLoginRequest(
            login = login,
            password = pwd,
            activeDirectoryFlag = typeAuthorization
        )

        val jsonString: String = Assistant.toJson(payload)

        val config = ConfigToRequest(
            "UserAutorization",
            "actionLogin",
            "",
            jsonString
        )

        cancelJobs("requestSession")

        val job = viewModelCoroutineScope.launch {
            requestSession(config, network.getTypeRequest())
        }
        addJob("requestSession", job)
    }

    fun initDepartments() {
        Log.println(Log.INFO, "storeCompanyApp.requestDepartments", "Запрос уведомлений на сервере")

        val config = ConfigToRequest(
            "handbooks\\Department",
            "GetDepartmentList",
            "",
            ""
        )

        cancelJobs("requestDepartments")

        val job = viewModelCoroutineScope.launch { requestDepartments(config, network.getTypeRequest()) }
        addJob("requestDepartments", job)

        Log.println(Log.INFO, "storeCompanyApp.requestDepartments", "Закончил выполнение: ")
    }

    data class UserAutorizationActionLoginRequest(
        val login: String,
        val password: String,
        val activeDirectoryFlag: Boolean
    )

    fun checkUserSession(): Boolean {
        var statusSession = false
        if (userSession.value != null && userSession.value?.worker_id != -1) {
            statusSession = true
        }
        return statusSession
    }

    private suspend fun requestSession(configToRequest: ConfigToRequest, isOnline: String) =
        withContext(Dispatchers.IO) {
            class Token : TypeToken<JsonFromServer<UserSession>>()

            val response = interactor.getData(configToRequest, isOnline)
            cancelJobs("requestSession:SaveHandbookData")
            val job = viewModelCoroutineScope.launch {
                interactor.saveHandbookData(configToRequest.method, response)
            }
            addJob("requestSession:SaveHandbookData", job)
            val temp: JsonFromServer<UserSession> = Gson().fromJson(response, Token().type)
            userSession.postValue(temp.getItems())
        }

    private suspend fun requestDepartments(configToRequest: ConfigToRequest, isOnline: String) =
        withContext(Dispatchers.IO) {
            class Token : TypeToken<JsonFromServer<ArrayList<Company>>>()

            val response = interactor.getData(configToRequest, isOnline)
            cancelJobs("requestDepartments:SaveHandbookData")

            val job = viewModelCoroutineScope.launch { interactor.saveHandbookData(configToRequest.method, response) }
            addJob("requestDepartments:SaveHandbookData", job)

            val temp: JsonFromServer<ArrayList<Company>> = Gson().fromJson(response, Token().type)
            departmentList.postValue(temp.getItems())
        }

    override fun handleError(error: Throwable) {
        Log.println(
            Log.ERROR,
            "storeCompanyApp.handleError",
            error.message.toString()
        )
    }

    fun searchInDepartmentList(query: String): Flow<ArrayList<Company>> {
        return flow {
            var result: ArrayList<Company> = ArrayList()
            if (departmentList.value != null) {
                result = departmentList.value!!.filter { company -> company.title.indexOf(query) > -1 } as ArrayList<Company>
            }
            if (result.isEmpty()) {
                result.add(Company(0, "Результат", 0, ArrayList(), ArrayList()))
            }
            emit(result)
        }
    }
}