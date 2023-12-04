package com.e.companyapp.view.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.e.companyapp.R
import com.e.companyapp.databinding.MainMenuFragmentBinding
import com.e.companyapp.viewmodel.StoreCompany
import com.example.models.Company
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Главное меню
 */
class MainMenuFragment : Fragment() {

    private var _binding: MainMenuFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var storeCompany: StoreCompany

    private val job: Job = Job()
    private val queryStateFlow = MutableStateFlow("")
    private lateinit var searchView: SearchView
    private lateinit var searchResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainMenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: StoreCompany by viewModel()
        storeCompany = viewModel
        initFragment()

        searchView = binding.searchView
        searchResult = binding.txtSearchResult
        initSearch(searchView, searchResult)

    }

    /**
     * Метод инициализации фрагмента
     */
    private fun initFragment() {
        childFragmentManager.beginTransaction()                                                                                                                    // поверх открываем всплывающее окно, которое закроется через 5 секунд
            .add(
                R.id.containerAppBar, AppBarTopMainFragment.newInstance(
                    "Company_app",
                    com.example.config.Const.APP_BAR_MAIN,
                    "",
                    ""
                )
            )
            .commitNow()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initSearch(searchView: SearchView, searchResult: TextView) {
        storeCompany.initDepartments()

        // инициализация корутины поиска
        CoroutineScope(Dispatchers.Main + job).launch {
            queryStateFlow.debounce(500)
                .filter { query ->
                    if (query.isEmpty()) {
                        searchResult.text = ""
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    storeCompany.searchInDepartmentList(query)
                        .catch {
                            var depList: ArrayList<Company> = ArrayList()
                            depList.add(Company(0, "Ошибка", 0, ArrayList(), ArrayList()))
                            emit(depList)
                        }
                }
                .collect { result ->

                    searchResult.text = result[0].title
                }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { queryStateFlow.value = it }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                queryStateFlow.value = newText
                return true
            }
        })
    }

    override fun onDestroyView() {
        job.cancel()
        _binding = null
        super.onDestroyView()
    }
}