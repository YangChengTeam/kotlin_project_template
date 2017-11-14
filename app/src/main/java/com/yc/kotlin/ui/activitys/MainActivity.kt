package com.yc.kotlin.ui.activitys

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.yc.kotlin.R
import com.yc.kotlin.databinding.ActivityMainBinding
import com.yc.kotlin.di.compoent.DaggerMainActivityComponent
import com.yc.kotlin.di.module.MainActivityModule
import com.yc.kotlin.domin.kmapOf
import com.yc.kotlin.ui.wdigets.adapters.MainAdapter
import com.yc.kotlin.ui.wdigets.views.MultiStateView
import com.yc.kotlin.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject lateinit var mainAdapter: MainAdapter
    @Inject lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        DaggerMainActivityComponent.builder().mainActivityModule(MainActivityModule(this)).build().inject(this)

        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING.toInt())
        stateView.setStateListener(object : MultiStateView.StateListener{
            override fun onStateChanged(viewState: Int) {

            }
        })

        recyclerView.adapter = mainAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getNewsInfo(kmapOf(
                "news_id" to "927",
                "period" to "-1")).observe(this, Observer {
            bind.title = it?.title
            mainAdapter.setNewData(listOf(it))
            stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT.toInt())
        })
    }
}




