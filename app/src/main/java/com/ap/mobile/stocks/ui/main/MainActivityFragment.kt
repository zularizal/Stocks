package com.ap.mobile.stocks.ui.main

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.data.local.entity.UserStockList
import com.ap.mobile.stocks.databinding.FragmentMainBinding
import com.ap.mobile.stocks.databinding.RecyclerviewStocksListBinding
import com.ap.mobile.stocks.ui.base.BaseFragment
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RxSimpleAdapter
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable

/**
 * A placeholder fragment containing a simple view.
 */
@Suppress("unused")
class MainActivityFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    companion object {
        private val TAG = MainActivityFragment::class.java.simpleName
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        val searchViewItem = menu?.findItem(R.id.action_search)
        // Get the SearchView and set the searchable configuration
        val searchView = searchViewItem?.actionView as SearchView

        setupSearchView(searchView)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        viewModel.getUserStockList().observe(
            this , Observer {
                    if(it != null) {
                        dataBinding.stocksFavRecyclerView.apply {
                            adapter = RxSimpleAdapter<UserStockList, RecyclerviewStocksListBinding>(
                                Observable.fromArray(viewModel.userStocksList.value!!),
                                viewModel.userStocksList.value!!,
                                RecyclerviewStocksListBinding::inflate,
                                RecyclerviewStocksListBinding::setViewModel,
                                UserStockList::uid
                            ).apply {
                                clicks.bindToLifecycle(view!!).subscribe {
                                    Snackbar.make(view!!, it.symbol, Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
        })

    }

    private fun setupSearchView(searchView: SearchView) {
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.queryHint = "Search Stock Symbol"
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.setIconifiedByDefault(true)
        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // **Here you can get the value "query" which is entered in the search box.**

                Toast.makeText(context, "symbol :$query", Toast.LENGTH_LONG).show()
                viewModel.getStockData(symbol = query).observe(this@MainActivityFragment, Observer {
                    if(it != null) {
                        viewModel.insertStockToUserList(UserStockList(0, it.company?.companyName!!, it.company.symbol!!))
                        if(viewModel.userStocksList.value == null) {
                            setupRecyclerView()
                        }
                        Snackbar.make(view!!, "high: ${it.quote?.high}, low: ${it.quote?.low}, current: ${it.quote?.latestPrice}", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(view!!, "Symbol Not Found", Snackbar.LENGTH_SHORT).show()
                    }
                })
                searchView.clearFocus()
                return true
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)
    }


}