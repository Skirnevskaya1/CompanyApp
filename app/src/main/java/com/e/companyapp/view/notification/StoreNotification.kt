package com.e.companyapp.view.notification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.utils.network.Network
import com.e.companyapp.interactor.MainInteractor
import com.e.companyapp.viewmodel.BaseViewModel
import com.example.models.ConfigToRequest
import com.example.models.JsonFromServer
import com.example.models.Notification
import com.example.models.NotificationList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class StoreNotification(

    private val interactor: MainInteractor,
    private val network: Network,
    private var notificationAll: MutableLiveData<ArrayList<NotificationList<Notification>>> = MutableLiveData(),
) : BaseViewModel() {

    fun getNotificationAll() = notificationAll
    fun getNotificationPersonal(): MutableLiveData<ArrayList<NotificationList<Notification>>> {
        return notificationAll
    }

    fun initNotifications(companyId: Int?) {
        Log.println(Log.INFO, "storeCompany.getNotification", "Запрос уведомлений на сервере")
        Log.println(Log.INFO, "storeCompany.getNotification", "companyId: $companyId")

        val payload = NotificationAllRequest(
            company_id = companyId
        )

        val jsonString: String = com.example.utils.Assistant.toJson(payload)

        val config = ConfigToRequest(
            "notification\\Notification",
            "GetNotificationAll",
            "",
            jsonString
        )

        cancelJobs("requestNotification")

        val job = viewModelCoroutineScope.launch() { requestNotification(config, network.getTypeRequest()) }
        addJob("requestNotification", job)

        Log.println(Log.INFO, "storeCompany.getNotification", "Закончил выполнение: ")
    }

    data class NotificationAllRequest(
        val company_id: Int?
    )

    private suspend fun requestNotification(configToRequest: ConfigToRequest, isOnline: String) =
        withContext(Dispatchers.IO) {
            class Token : TypeToken<JsonFromServer<ArrayList<NotificationList<Notification>>>>()

            val response = interactor.getData(configToRequest, isOnline)
            cancelJobs("requestNotification:SaveHandbookData")

            val job = viewModelCoroutineScope.launch { interactor.saveHandbookData(configToRequest.method, response) }
            addJob("requestNotification:SaveHandbookData", job)

            val temp: JsonFromServer<ArrayList<NotificationList<Notification>>> = Gson().fromJson(response, Token().type)
            notificationAll.postValue(temp.getItems())
        }

    override fun handleError(error: Throwable) {
        Log.println(
            Log.ERROR,
            "storeCompany.handleError",
            error.message.toString()
        )
    }

}