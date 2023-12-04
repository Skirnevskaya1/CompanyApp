package com.e.companyapp.view.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.companyapp.view.notification.adapters.RvGroupNotificationAdapter
import com.e.companyapp.databinding.GroupNotificationFragmentBinding
import com.e.companyapp.view.BaseFragment
import com.example.models.Notification
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupNotificationFragment : BaseFragment<Notification>() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: GroupNotificationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationStore: StoreNotification
    private lateinit var notificationScopeInstance: Scope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GroupNotificationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationScopeInstance = KoinJavaComponent.getKoin().getOrCreateScope(
            "notificationScopeId", named("NOTIFICATION_STORE"))
        notificationStore = notificationScopeInstance.get()

        val rvGroupNotification = binding.rvGroupNotification
        rvGroupNotification.layoutManager = LinearLayoutManager(requireContext())

        rvGroupNotification.adapter = notificationStore.getNotificationAll().value?.let { RvGroupNotificationAdapter(it) }

        notificationStore.getNotificationAll().observe(viewLifecycleOwner, {
            rvGroupNotification.adapter = RvGroupNotificationAdapter(it)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupNotificationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}