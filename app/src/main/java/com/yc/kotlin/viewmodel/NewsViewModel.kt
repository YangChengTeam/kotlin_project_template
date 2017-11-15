package com.yc.kotlin.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.yc.kotlin.domin.*
import com.yc.kotlin.repository.net.ApiService
import com.yc.kotlin.ui.wdigets.views.MultiStateView
import com.yc.kotlin.utils.rxrun
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by zhangkai on 2017/11/13.
 */
class NewsViewModel : BaseViewModel<NewsInfoWrapper>() {
    val newsInfoDao by lazy {
        appDatabase.newsInfoDao()
    }

    val apiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val data = MediatorLiveData<List<NewsInfo>>()

    fun getNewsInfo(type: String, refresh: Boolean = false): LiveData<List<NewsInfo>> {
        if (!refresh) {
            val cache = newsInfoDao.loadAll()
            if (cache != null) {
                viewStateCommand.value = MultiStateView.VIEW_STATE_CONTENT.toInt()
                data.addSource(cache,  {
                    data.value = it
                    data.removeSource(cache)
                })
                return data
            }
        }
        val params = kmapOf(
                "page" to "1",
                "type_id" to type)
        val disposable = apiService.getNewsInfo(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers
                .mainThread()).subscribe({
            onSuccess(it, {
                if (it?.data?.list?.size > 0) {
                    data.value = it.data.list
                    rxrun {
                        newsInfoDao?.insert(it.data.list)
                    }
                    viewStateCommand.value = MultiStateView.VIEW_STATE_CONTENT.toInt()
                } else {
                    viewStateCommand.value = MultiStateView.VIEW_STATE_EMPTY.toInt()
                }
            })
        }, {
            onError(it)
        })
        disposables.add(disposable)

        return data
    }

}