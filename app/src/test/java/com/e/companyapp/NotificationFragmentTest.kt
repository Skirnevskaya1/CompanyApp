package com.e.companyapp


import androidx.fragment.app.testing.FragmentScenario

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.e.companyapp.view.notification.NotificationFragment
import com.google.android.material.tabs.TabLayout
import junit.framework.Assert.assertEquals

import org.junit.After

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(
    sdk = [30],
    instrumentedPackages = ["androidx.loader.content"]
)
class NotificationFragmentTest {
    lateinit var scenario: FragmentScenario<NotificationFragment>

    //
    @Before
    fun setup() {
        scenario = FragmentScenario.launch(NotificationFragment::class.java)
    }


    @Test
    fun `test tabNotifications grouptab`() {
        lateinit var tabNotifications: TabLayout

        scenario.onFragment {
            tabNotifications = it.getView()?.findViewById(R.id.tabNotifications)!!
        }

        tabNotifications.selectTab(tabNotifications.getTabAt(0))
        
        assertEquals(tabNotifications.selectedTabPosition, 0)

    }

    @Test
    fun `test tabNotifications personaltab`() {
        lateinit var tabNotifications: TabLayout

        scenario.onFragment {
            tabNotifications = it.getView()?.findViewById(R.id.tabNotifications)!!
        }
        tabNotifications.selectTab(tabNotifications.getTabAt(1))

        assertEquals(tabNotifications.selectedTabPosition, 1)

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @After
    fun close() {
        scenario.close()
    }
}