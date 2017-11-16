package com.yc.kotlin.ui.activitys

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.yc.kotlin.R
import com.yc.kotlin.databinding.ActivityMainBinding
import com.yc.kotlin.domin.ARouterPath
import com.yc.kotlin.domin.NewsInfo
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by zhangkai on 2017/11/16.
 */


@Route(path = ARouterPath.TEST_ACTIVITY)
class TestActivity : Activity() {
    @Autowired
    lateinit var newsInfo: NewsInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bind = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_test)

        ARouter.getInstance().inject(this)
        Log.i(ARouterPath.TEST_ACTIVITY, newsInfo.title)
        textView.text = newsInfo.title
    }
}