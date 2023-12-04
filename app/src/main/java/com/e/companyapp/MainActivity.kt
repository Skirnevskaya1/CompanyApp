package com.e.companyapp

import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.e.companyapp.databinding.ActivityMainBinding
import com.e.companyapp.view.menu.IAppBarTopMain
import com.e.companyapp.view.menu.IAppMain
import com.e.companyapp.view.menu.IAppMainMenu
import com.e.companyapp.view.menu.NavigationMainMenuFragment
import com.e.companyapp.view.startApplication.AuthorizationFragment
import com.e.companyapp.view.startApplication.SplashScreenFragment
import com.e.companyapp.viewmodel.StoreCompany
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), IAppBarTopMain, IAppMainMenu, IAppMain {

    private lateinit var storeCompany: StoreCompany
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setDefaultSplashScreen()
        setContentView(view)
        initViewModel()

        if (savedInstanceState == null) {
            if (!storeCompany.checkUserSession()) {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .add(R.id.container, AuthorizationFragment.newInstance())
                    .commitNow()
            }
        }
    }

    private fun setDefaultSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setSplashScreenHideAnimation()
            setSplashScreenDuration()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SplashScreenFragment.newInstance())
                .commitNow()
        }

    }

    @RequiresApi(31)
    private fun setSplashScreenHideAnimation() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideLeft = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideLeft.interpolator = AnticipateInterpolator()
            slideLeft.duration = SLIDE_LEFT_DURATION
            slideLeft.doOnEnd { splashScreenView.remove() }
            slideLeft.start()
        }
    }

    private fun setSplashScreenDuration() {
        var isHideSplashScreen = false
        object : CountDownTimer(COUNTDOWN_DURATION, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                isHideSplashScreen = true
            }
        }.start()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isHideSplashScreen) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    private fun initViewModel() {
        val viewModel: StoreCompany by viewModel()
        storeCompany = viewModel
    }

    /**
     * Инициализация приложения при успешной авториазции
     */
    override fun initApp(string: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.navigationMainMenuContainer, NavigationMainMenuFragment.newInstance())
            .commitNow()
    }


    override fun openMainMenu(string: String) {
        binding.drawerMainMenu.openDrawer(GravityCompat.START)
    }


    override fun openFragment(nameFragment: String) {
        if (binding.drawerMainMenu.isDrawerOpen(GravityCompat.START)) {
            binding.drawerMainMenu.closeDrawer(GravityCompat.START)
        }
        when (nameFragment) {
            "NotificationFragment" -> openNotifications("NotificationFragment")
            "MainFragment" -> openMain("MainFragment")
        }
    }

    override fun openNotifications(string: String) {
        binding.container.findNavController().navigate(R.id.notificationFragment)
    }

    override fun openMain(nameFragment: String) {
        binding.container.findNavController().navigate(R.id.mainFragment)
    }

    override fun backFragment(nameFragment: String) {
        binding.container.findNavController().popBackStack()
    }

    companion object {
        private const val SLIDE_LEFT_DURATION = 700L
        private const val COUNTDOWN_DURATION = 500L
        private const val COUNTDOWN_INTERVAL = 100L
    }

}