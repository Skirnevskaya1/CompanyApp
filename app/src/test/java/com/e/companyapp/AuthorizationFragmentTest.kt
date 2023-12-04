package com.e.companyapp


import androidx.fragment.app.testing.FragmentScenario

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.e.companyapp.view.startApplication.AuthorizationFragment
import com.google.android.material.textfield.TextInputEditText
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
class AuthorizationFragmentTest {
    lateinit var scenario: FragmentScenario<AuthorizationFragment>

    //
    @Before
    fun setup() {
        scenario = FragmentScenario.launch(AuthorizationFragment::class.java)
    }

    @Test
    fun `test empty text in login field`() {
        lateinit var txtLogin: TextInputEditText

        scenario.onFragment {
            txtLogin = it.getView()?.findViewById(R.id.txtLogin)!!
        }

        assertEquals(txtLogin.text.toString(), "")

    }

    @Test
    fun `test empty text in pwd field`() {
        lateinit var txtPwd: TextInputEditText

        scenario.onFragment {
            txtPwd = it.getView()?.findViewById(R.id.txtPwd)!!
        }

        assertEquals(txtPwd.text.toString(), "")

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