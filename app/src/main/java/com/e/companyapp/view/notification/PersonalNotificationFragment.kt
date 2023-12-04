package com.e.companyapp.view.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.companyapp.R
import com.e.companyapp.view.BaseFragment
import com.e.companyapp.view.notification.adapters.RvPersonalNotificationAdapter
import com.e.companyapp.view.viewById
import com.example.models.Notification
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

class PersonalNotificationFragment : BaseFragment<Notification>() {
    private lateinit var notificationStore: StoreNotification
    private lateinit var notificationScopeInstance: Scope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.personal_notification_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationScopeInstance = KoinJavaComponent.getKoin().getOrCreateScope("notificationScopeId", named("NOTIFICATION_STORE"))
        notificationStore = notificationScopeInstance.get()

        val rvPersonalNotification by viewById<RecyclerView>(R.id.rvPersonalNotification)
        rvPersonalNotification.layoutManager = LinearLayoutManager(requireContext())

        rvPersonalNotification.adapter = notificationStore.getNotificationPersonal().value?.let { RvPersonalNotificationAdapter(it) }

        notificationStore.getNotificationAll().observe(viewLifecycleOwner, {
            rvPersonalNotification.adapter = RvPersonalNotificationAdapter(it)
        })
    }
}