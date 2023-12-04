package com.e.companyapp.view.startApplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.e.companyapp.databinding.SplashScreenFragmentBinding
import com.e.companyapp.view.BaseFragment
import com.example.models.Notification

/**
 * Страница авторизации пользователя в системе
 */
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<Notification>() {

    private var _binding: SplashScreenFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgLogoSplash = binding.imgLogoSplash
        val tvTopTextView = binding.tvTopTextView
        val tvBottomTextView = binding.tvBottomTextView

        val alphaStart = 0.3f
        val alphaEnd = 1f
        val durationAnimation = 1500L

        createAnimation(imgLogoSplash, alphaStart, alphaEnd, durationAnimation)                     // центральный лого
        createAnimation(tvTopTextView, alphaStart, alphaEnd, durationAnimation)                     // верхняя строчка
        createAnimation(tvBottomTextView, alphaStart, alphaEnd, durationAnimation)                  // нижняя строчка


        Handler(Looper.getMainLooper()).postDelayed({
            parentFragmentManager.beginTransaction().remove(this).commitNow()
        }, 3000)
    }

    /**
     * Метод установки анимации на элементы заставки
     */
    private fun createAnimation(element: View, alphaStart: Float, alphaEnd: Float, durationAnimation: Long) {
        element.alpha = alphaStart
        element.animate()
            .alpha(alphaEnd)
            .setDuration(durationAnimation)
            .setListener(null)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = SplashScreenFragment()
    }
}