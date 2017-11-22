package com.yc.kotlin.ui.activitys

import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.yc.kotlin.R
import com.yc.kotlin.databinding.ActivityMainBinding
import com.yc.kotlin.di.component.DaggerMainActivityComponent
import com.yc.kotlin.di.module.MainActivityModule
import com.yc.kotlin.domin.ARouterPath
import com.yc.kotlin.domin.Config
import com.yc.kotlin.pay.IPayAbs
import com.yc.kotlin.pay.di.component.DaggerPayComponent
import com.yc.kotlin.pay.di.module.PayAbsModule
import com.yc.kotlin.pay.domin.OrderInfo
import com.yc.kotlin.pay.domin.OrderParamsInfo
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

    @Inject lateinit var ipayAbs: IPayAbs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        DaggerMainActivityComponent.builder().payAbsModule(PayAbsModule(this)).mainActivityModule(MainActivityModule
        (this)).build()
                .inject(this)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary))
        swipeRefreshLayout.refreshes().observeOn(AndroidSchedulers.mainThread()).subscribe {
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
            if (it?.size == 20) {
                mainAdapter.loadMoreComplete()
            } else {
                mainAdapter.loadMoreEnd()
            }
            swipeRefreshLayout.isRefreshing = false
        })

        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING.toInt())
        viewModel.viewStateCommand.observe(this, Observer {
            if (page == 1) {
                stateView.setViewState(it ?: MultiStateView.VIEW_STATE_UNKNOWN.toInt())
            }
            if (it!!.toLong() != MultiStateView.VIEW_STATE_CONTENT) {
                mainAdapter.loadMoreEnd()
            }
            swipeRefreshLayout.isRefreshing = false
        })


        mainAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener{
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity,
                        view!!.findViewById(R.id.title), getString(R.string.transition))
                ARouter.getInstance().build(ARouterPath.TEST_ACTIVITY).withParcelable("newsInfo", mainAdapter?.data?.get(position)).withOptionsCompat(compat).navigation(this@MainActivity)
            }
        })

        val orderParams = OrderParamsInfo(Config.baseUrl +"order/init", "1", "1", 2f, "test")
        orderParams.paywayName = "h5_"
        ipayAbs.pay(orderParams, object : IPayAbs.IPayCallback {
            override fun onSuccess(orderInfo: OrderInfo) {
            }

            override fun onFailure(orderInfo: OrderInfo) {
            }
        })
    }
}




