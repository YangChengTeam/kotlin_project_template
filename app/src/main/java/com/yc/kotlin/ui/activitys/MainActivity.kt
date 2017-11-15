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
import com.yc.kotlin.domin.kmapOf
import com.yc.kotlin.ui.wdigets.adapters.MainAdapter
import com.yc.kotlin.ui.wdigets.views.MultiStateView
import com.yc.kotlin.viewmodel.NewsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject lateinit var mainAdapter: MainAdapter
    @Inject lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        DaggerMainActivityComponent.builder().mainActivityModule(MainActivityModule(this)).build().inject(this)

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        RxSwipeRefreshLayout.refreshes(swipeRefreshLayout).observeOn(AndroidSchedulers.mainThread()).subscribe {
            viewModel.getNewsInfo(Random().nextInt(10).toString(), true)
        }

        recyclerView.adapter = mainAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getNewsInfo("20").observe(this, Observer {
            mainAdapter.setNewData(it)
            swipeRefreshLayout.isRefreshing = false
        })

        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING.toInt())
        viewModel.viewStateCommand.observe(this, Observer {
            stateView.setViewState(it ?: MultiStateView.VIEW_STATE_UNKNOWN.toInt())
            swipeRefreshLayout.isRefreshing = false
        })
    }
}




