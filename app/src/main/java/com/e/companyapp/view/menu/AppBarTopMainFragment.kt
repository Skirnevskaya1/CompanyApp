package com.e.companyapp.view.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.config.Const
import com.e.companyapp.databinding.AppBarTopMainFragmentBinding
import com.e.companyapp.viewmodel.StoreCompany
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val APP_BAR_TITLE = "appBarTitle"
private const val APP_BAR_TYPE = "appBarType"
private const val APP_BAR_PATH_BACK = "appBarPathBack"
private const val APP_BAR_PATH_APPLY = "appBarPathApply"

class AppBarTopMainFragment : Fragment() {

    private var title: String? = null
    private var appBarType: Int? = null
    private var appBarPathBack: String? = null
    private var appBarPathApply: String? = null

    private var _binding: AppBarTopMainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var storeCompany: StoreCompany

    private var mCallback: IAppMainMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(APP_BAR_TITLE)
            appBarType = it.getInt(APP_BAR_TYPE)
            appBarPathBack = it.getString(APP_BAR_PATH_BACK)
            appBarPathApply = it.getString(APP_BAR_PATH_APPLY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AppBarTopMainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: StoreCompany by viewModel()
        storeCompany = viewModel

        binding.tvAppBarTitle.text = title

        when (appBarType) {
            Const.APP_BAR_MAIN -> {
                binding.btnAppBarMainMenu.visibility = VISIBLE
                binding.btnAppBarNotification.visibility = VISIBLE
                binding.btnAppBarSearch.visibility = INVISIBLE
                binding.btnAppBarBack.visibility = INVISIBLE
                binding.btnAppBarApply.visibility = INVISIBLE
            }
            Const.APP_BAR_SECOND -> {
                binding.btnAppBarMainMenu.visibility = INVISIBLE
                binding.btnAppBarNotification.visibility = INVISIBLE
                binding.btnAppBarSearch.visibility = VISIBLE
                binding.btnAppBarBack.visibility = VISIBLE
                binding.btnAppBarApply.visibility = VISIBLE
            }
            Const.APP_BAR_MODAL -> {
                binding.btnAppBarMainMenu.visibility = INVISIBLE
                binding.btnAppBarNotification.visibility = INVISIBLE
                binding.btnAppBarSearch.visibility = INVISIBLE
                binding.btnAppBarApply.visibility = VISIBLE
                binding.btnAppBarBack.visibility = VISIBLE
            }
            Const.APP_BAR_ONLY_BACK -> {
                binding.btnAppBarMainMenu.visibility = INVISIBLE
                binding.btnAppBarNotification.visibility = INVISIBLE
                binding.btnAppBarSearch.visibility = INVISIBLE
                binding.btnAppBarApply.visibility = INVISIBLE
                binding.btnAppBarBack.visibility = VISIBLE
            }
        }

        binding.btnAppBarBack.setOnClickListener {
            when (appBarType) {
                Const.APP_BAR_MAIN, Const.APP_BAR_ONLY_BACK, Const.APP_BAR_SECOND -> {
                    mCallback!!.backFragment("BackFromFragment")
                }
                Const.APP_BAR_MODAL -> {
                    mCallback!!.backFragment("BackFromModal")
                }
            }
        }

        binding.btnAppBarMainMenu.setOnClickListener {
            mCallback!!.openMainMenu("MainMenu")
        }

        binding.btnAppBarNotification.setOnClickListener {
            mCallback!!.openFragment("NotificationFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title заголовок компонента.
         * @param typeAppBar тип компонента
         * @param appBarPathBack путь назад.
         * @param appBarPathApply путь вперед.
         * @return A new instance of fragment AppBarTopMainFragment.
         */

        @JvmStatic
        fun newInstance(
            title: String,
            typeAppBar: Int,
            appBarPathBack: String,
            appBarPathApply: String
        ) =
            AppBarTopMainFragment().apply {
                arguments = Bundle().apply {
                    putString(APP_BAR_TITLE, title)
                    putInt(APP_BAR_TYPE, typeAppBar)
                    putString(APP_BAR_PATH_BACK, appBarPathBack)
                    putString(APP_BAR_PATH_APPLY, appBarPathApply)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallback = context as IAppMainMenu
        } catch (e: Exception) {
            Log.println(Log.ERROR, "AppBarTopMain.onAttach", "Ошибка")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }
}