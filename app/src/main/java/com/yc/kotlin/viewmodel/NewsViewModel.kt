package com.yc.kotlin.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.yc.kotlin.domin.*
import com.yc.kotlin.repository.net.ApiService
import com.yc.kotlin.ui.wdigets.views.MultiStateView
import com.yc.kotlin.utils.rxrun
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by zhangkai on 2017/11/13.
 */
class NewsViewModel : StateViewModel<NewsInfoWrapper>() {
    val newsInfoDao by lazy {
        appDatabase.newsInfoDao()
    }

    val apiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun getNewsInfo(params: Map<String, String?>): LiveData<List<NewsInfo>> {
        val cache = newsInfoDao.loadAll()
        if( cache!= null){
            stateCommand.value = MultiStateView.VIEW_STATE_CONTENT.toInt()
            return cache
        }
        var data = MediatorLiveData<List<NewsInfo>>()
        apiService.getNewsInfo(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            onSuccess(it, {
                if (it?.data?.list?.size > 0) {
                    data.value = it.data.list
                    rxrun {
                        newsInfoDao?.insert(it.data.list)
                    }
                    stateCommand.value = MultiStateView.VIEW_STATE_CONTENT.toInt()
                } else {
                    stateCommand.value = MultiStateView.VIEW_STATE_EMPTY.toInt()
                }
            })
        }, {
            onError(it)
        })
        return data
    }

}