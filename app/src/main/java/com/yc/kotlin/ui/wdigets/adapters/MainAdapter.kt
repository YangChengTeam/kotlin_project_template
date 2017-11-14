package com.yc.kotlin.ui.wdigets.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import android.view.ViewGroup
import com.yc.kotlin.BR
import com.yc.kotlin.R
import com.yc.kotlin.domin.NewsInfo


/**
 * Created by zhangkai on 2017/11/10.
 */
class MainAdapter : BaseQuickAdapter<NewsInfo, MainAdapter.ViewHolder>(R.layout.item) {

    override fun convert(helper: ViewHolder?, item: NewsInfo?) {
        val binding = helper?.binding
        binding?.setVariable(BR.news, item)
        binding?.executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false) ?: return super
                .getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val binding: ViewDataBinding
            get() = getConvertView().getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
    }

}



