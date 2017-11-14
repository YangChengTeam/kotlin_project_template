package com.yc.kotlin.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.yc.kotlin.domin.Config
import com.yc.kotlin.domin.ResultInfo
import com.yc.kotlin.ui.wdigets.views.MultiStateView
import com.yc.kotlin.utils.rxrun

/**
 * Created by zhangkai on 2017/11/14.
 */
open class StateViewModel<T> : ViewModel() {
    val stateCommand = MutableLiveData<Int>()

    fun onError(t: Throwable) {
        stateCommand.value = MultiStateView.VIEW_STATE_ERROR.toInt()
    }

    fun onSuccess(resultInfo: ResultInfo<T>, func: (resultInfo: ResultInfo<T>) -> Unit) {
        if (resultInfo?.code == Config.HTTP_STAUTS_OK) {
            func(resultInfo)
        } else {
            stateCommand.value = MultiStateView.VIEW_STATE_ERROR.toInt()
        }
    }
}