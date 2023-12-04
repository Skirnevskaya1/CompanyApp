package com.e.companyapp.interactor

import com.example.models.ConfigToRequest
import java.util.*

interface IInteractor {
    suspend fun getData(configToRequest: ConfigToRequest, modeRequest: String): String
    suspend fun saveHandbookData(nameMethod: String, json: String)
    suspend fun saveModuleData(period: String, date: Date, shift: Int, companyId: Int, methodName: String, json: String)

}