package com.e.companyapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    var jobs = mutableMapOf<String, MutableList<Job>>()

    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    protected fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    protected fun cancelJobs(nameCoroutine: String) {
        if (jobs.containsKey(nameCoroutine)) {
            for (job in jobs[nameCoroutine]!!) {
                job.cancel()
            }
            jobs.remove(nameCoroutine)
        }
    }

    protected fun addJob(nameCoroutine: String, job: Job) {
        if (!jobs.containsKey(nameCoroutine)) {
            jobs[nameCoroutine] = mutableListOf()
        }

        jobs[nameCoroutine]!!.add(job)
    }

    abstract fun handleError(error: Throwable)
}