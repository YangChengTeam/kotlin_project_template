package com.yc.kotlin.ui.activitys

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.yc.kotlin.R
import com.yc.kotlin.databinding.ActivityMainBinding
import com.yc.kotlin.di.compoent.DaggerMainActivityComponent
import com.yc.kotlin.di.module.MainActivityModule
import com.yc.kotlin.ui.wdigets.adapters.MainAdapter
import com.yc.kotlin.ui.wdigets.views.MultiStateView
import com.yc.kotlin.viewmodel.NewsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    var page = 1
    @Inject lateinit var mainAdapter: MainAdapter
    @Inject lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        DaggerMainActivityComponent.builder().mainActivityModule(MainActivityModule(this)).build().inject(this)

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        RxSwipeRefreshLayout.refreshes(swipeRefreshLayout).observeOn(AndroidSchedulers.mainThread()).subscribe {
            page = 1
            viewModel.getNewsInfo(Random().nextInt(10).toString(), refresh = true)
        }

        recyclerView.adapter = mainAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        mainAdapter.setEnableLoadMore(true)
        mainAdapter.setOnLoadMoreListener({
            viewModel.getNewsInfo(Random().nextInt(10).toString(), ++page, true)
        }, recyclerView)

        viewModel.getNewsInfo("20").observe(this, Observer {
            if (page == 1) {
                mainAdapter.setNewData(it)
            } else {
                mainAdapter.addData(it)
            }
            if(it?.size == 20){
                mainAdapter.loadMoreComplete()
            } else {
                mainAdapter.loadMoreEnd()
            }
            swipeRefreshLayout.isRefreshing = false
        })

        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING.toInt())
        viewModel.viewStateCommand.observe(this, Observer {
            if(page == 1) {
                stateView.setViewState(it ?: MultiStateView.VIEW_STATE_UNKNOWN.toInt())
            }
            if(it!!.toLong() != MultiStateView.VIEW_STATE_CONTENT){
                mainAdapter.loadMoreEnd()
            }
            swipeRefreshLayout.isRefreshing = false
        })

    }
}




