package com.e.companyapp.view.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.companyapp.R
import com.e.companyapp.view.notification.adapters.VpNotificationAdapter
import com.e.companyapp.databinding.NotificationFragmentBinding
import com.e.companyapp.view.BaseFragment
import com.e.companyapp.view.menu.AppBarTopMainFragment
import com.example.models.Notification
import com.example.models.NotificationList
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.getKoin

class NotificationFragment : BaseFragment<Notification>() {

    private var _binding: NotificationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationStore: StoreNotification
    private lateinit var notificationScopeInstance: Scope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationScopeInstance = getKoin().getOrCreateScope("notificationScopeId", named("NOTIFICATION_STORE"))
        notificationStore = notificationScopeInstance.get()

        initFragment()
        initObserve()
    }

    private fun initObserve() {
        notificationStore.getNotificationAll().observe(viewLifecycleOwner,
            {
            setGroupNotificationBadge(getNotificationAllSize(it))
        })
    }

    private fun getNotificationAllSize(notificationList: ArrayList<NotificationList<Notification>>?): Int {
        var sizeNotification = 0
        if (notificationList != null) {
            for (notificationListItem in notificationList) {
                sizeNotification += notificationListItem.notifications.size
            }
        }

        return sizeNotification
    }

    private fun setGroupNotificationBadge(sizeBadge: Int = 0) {
        val tabNotifications = binding.tabNotifications
        val groupNotification = tabNotifications.getTabAt(0)?.orCreateBadge

        if (sizeBadge > 0) {
            groupNotification!!.isVisible = true
            groupNotification.maxCharacterCount = 3
            groupNotification.number = sizeBadge
        } else {
            groupNotification!!.isVisible = false
        }
    }

    private fun initFragment() {

        notificationStore.initNotifications(storeCompany.getUserSession().value?.userCompanyId)

        childFragmentManager.beginTransaction()
            .add(
                R.id.containerAppBar, AppBarTopMainFragment.newInstance(
                    "Уведомления",
                    com.example.config.Const.APP_BAR_ONLY_BACK,
                    "",
                    ""
                )
            )
            .commitNow()

        val tabNotifications = binding.tabNotifications
        binding.vpNotificationsFragment.adapter = VpNotificationAdapter(childFragmentManager)
        tabNotifications.setupWithViewPager(binding.vpNotificationsFragment)


        setGroupNotificationBadge(getNotificationAllSize(notificationStore.getNotificationAll().value))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        notificationScopeInstance.close()
    }

    companion object {
        fun newInstance() = NotificationFragment()
    }
}