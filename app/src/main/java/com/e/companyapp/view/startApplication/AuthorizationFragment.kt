package com.e.companyapp.view.startApplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.e.companyapp.R
import com.e.companyapp.databinding.AuthorizationFragmentBinding
import com.e.companyapp.view.menu.IAppMain
import com.e.companyapp.view.BaseFragment
import com.example.config.Bootstrap
import com.example.config.Const
import com.example.models.UserSession

/**
 * Страница авторизации пользователя в системе
 */
class AuthorizationFragment : BaseFragment<UserSession>() {

    private var _binding: AuthorizationFragmentBinding? = null
    private val binding get() = _binding!!


    private var mCallback: IAppMain? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthorizationFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeCompany.getUserSession().observe(viewLifecycleOwner, {
            renderData(it)
        })

        binding.btnCloseApplication.setOnClickListener {                                            // обработка нажания кнопки закрыть
            closeApp()
        }

        binding.txtLogin.addTextChangedListener {                                                   // сброс вывода ошибки при начале ввода текста в поле логина
            binding.layoutLogin.error = null
        }

        binding.txtPwd.addTextChangedListener {                                                     // сброс вывода ошибки при начале ввода текста в поле пароля
            binding.layoutPwd.error = null
        }

        binding.btnLogin.setOnClickListener {                                                       // обработчик кнопки авторизации
            var statusCheckField = true                                                             // статус проверки полей авторизации

            // делаем проверку на пустое поле пароля, если оно пустое, то красим выводим ошибку
            if (binding.txtPwd.text?.isEmpty() == true) {
                statusCheckField = false
                binding.layoutPwd.error = "Пароль не может быть пустым!"
            } else {
                binding.layoutPwd.error = null
            }

            // делаем проверку на пустое поле логина, если оно пустое, то красим выводим ошибку
            if (binding.txtLogin.text?.isEmpty() == true) {
                statusCheckField = false
                binding.layoutLogin.error = "Логин не может быть пустым!"
            } else {
                binding.layoutLogin.error = null
            }

            // выполняем авторизацию
            if (statusCheckField || Bootstrap.TYPE_BUILD == Const.VERSION_DEBUG) {
                storeCompany.initLogin(
                    binding.txtLogin.text.toString(),
                    binding.txtPwd.text.toString(),
                    binding.checkBox.isChecked
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = AuthorizationFragment()
    }

    /**
     * Метод закрытия приложения
     */
    private fun closeApp() {
        activity?.finishAffinity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = context as IAppMain
        } catch (e: Exception) {
            Log.println(Log.ERROR, "AuthorizationFragment.onAttach", "Не смог откастовать контекст")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }


    private fun renderData(userSession: UserSession?) {
        if (userSession == null) {
            showErrorScreen(getString(R.string.empty_server_response_on_success))
            binding.layoutLogin.error = " "
            binding.layoutPwd.error = "Введены неверные логин / пароль"
        } else {
            showViewSuccess()
            binding.layoutLogin.error = null
            binding.layoutPwd.error = null
            mCallback!!.initApp("Init")                                                       // Инициализируем приложение
            parentFragmentManager.beginTransaction().remove(this).commitNow()
        }
    }

    private fun showErrorScreen(error: String?) {
        showViewError()

    }

    private fun showViewSuccess() {

    }

    private fun showViewLoading() {

    }

    private fun showViewError() {

    }

}