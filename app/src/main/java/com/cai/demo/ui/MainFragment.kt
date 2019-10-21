package com.cai.demo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cai.demo.R
import com.cai.demo.model.Museum
import kotlinx.android.synthetic.main.fragment_main.empty_tip
import kotlinx.android.synthetic.main.fragment_main.error_tip
import kotlinx.android.synthetic.main.fragment_main.progress_bar
import kotlinx.android.synthetic.main.fragment_main.recycler_view
import kotlinx.android.synthetic.main.fragment_main.search_view
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

  companion object {
    fun newInstance() = MainFragment()
  }

  // Lazy Inject ViewModel
  private val viewModel: MainViewModel by viewModel()

  private lateinit var adapter: MuseumAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    setupViewModel()
    setupUI()
  }

  private fun setupViewModel() {
//    viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(MainViewModel::class.java)

    // set observers
    viewModel.museums.observe(this, museumsObserver)
    viewModel.showEmptyTip.observe(this, emptyTipObserver)
    viewModel.showErrorTip.observe(this, errorTipObserver)
    viewModel.showLoading.observe(this, loadingObserver)

    viewModel.museums.value = emptyList()

    // load museum list
    viewModel.loadMuseums()
  }

  private val museumsObserver = Observer<List<Museum>> {
    recycler_view.visibility = View.VISIBLE
    adapter.update(it)
    // fix bug:
    if (search_view.query.isNotEmpty()) {
      adapter.filter.filter(search_view.query)
    }
  }

  private val emptyTipObserver = Observer<Boolean> {
    empty_tip.visibility = if (it) View.VISIBLE else View.GONE
  }

  private val errorTipObserver = Observer<Boolean> {
    error_tip.visibility = if (it) View.VISIBLE else View.GONE
  }

  private val loadingObserver = Observer<Boolean> {
    progress_bar.visibility = if (it) View.VISIBLE else View.GONE
  }

  private fun setupUI() {
    // setup recyclerView
    adapter = MuseumAdapter(viewModel.museums.value?.toMutableList() ?: mutableListOf())
    recycler_view.layoutManager = LinearLayoutManager(context)
    recycler_view.adapter = adapter
    // setup searchView
    search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return true
      }
    })
  }
}

class ViewModelFactory : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MainViewModel() as T
  }
}