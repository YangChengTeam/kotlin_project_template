package com.yc.kotlin.ui.wdigets.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.yc.kotlin.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by zhangkai on 2017/11/14.
 */

class MultiStateView : FrameLayout {
    companion object {
        const val VIEW_STATE_UNKNOWN = -1L  //视图未知错误

        const val VIEW_STATE_CONTENT = 0L   //视图获取到内容

        const val VIEW_STATE_ERROR = 1L     //视图请求状态错误

        const val VIEW_STATE_EMPTY = 2L     //视图请求数据为空

        const val VIEW_STATE_LOADING = 3L   //视图正在加载

        const val VIEW_STATE_NET_ERROR  = 4L //视图网络错误

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(VIEW_STATE_UNKNOWN, VIEW_STATE_CONTENT, VIEW_STATE_ERROR,
            VIEW_STATE_EMPTY,
            VIEW_STATE_LOADING, VIEW_STATE_NET_ERROR)
    annotation class ViewState

    private var mInflater: LayoutInflater? = null

    private var mContentView: View? = null

    private var mLoadingView: View? = null

    private var mErrorView: View? = null

    private var mEmptyView: View? = null

    private var mAnimateViewChanges = false

    private var mListener: StateListener? = null

    @ViewState
    private var mViewState = VIEW_STATE_UNKNOWN

    constructor(context: Context, attrs: AttributeSet?):  super(context, attrs) {
        init(attrs)

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int): super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mInflater = LayoutInflater.from(context)
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView)

        val loadingViewResId = a.getResourceId(R.styleable.MultiStateView_loadingView, -1)
        if (loadingViewResId > -1) {
            mLoadingView = mInflater!!.inflate(loadingViewResId, this, false)
            addView(mLoadingView, mLoadingView!!.layoutParams)
        }

        val emptyViewResId = a.getResourceId(R.styleable.MultiStateView_emptyView, -1)
        if (emptyViewResId > -1) {
            mEmptyView = mInflater!!.inflate(emptyViewResId, this, false)
            addView(mEmptyView, mEmptyView!!.layoutParams)
        }

        val errorViewResId = a.getResourceId(R.styleable.MultiStateView_errorView, -1)
        if (errorViewResId > -1) {
            mErrorView = mInflater!!.inflate(errorViewResId, this, false)
            addView(mErrorView, mErrorView!!.layoutParams)
        }

        val viewState = a.getInt(R.styleable.MultiStateView_viewState, VIEW_STATE_CONTENT.toInt())
        mAnimateViewChanges = a.getBoolean(R.styleable.MultiStateView_animateViewChanges, false)

        when (viewState) {
            VIEW_STATE_CONTENT.toInt() -> mViewState = VIEW_STATE_CONTENT

            VIEW_STATE_ERROR.toInt() -> mViewState = VIEW_STATE_ERROR

            VIEW_STATE_EMPTY.toInt() -> mViewState = VIEW_STATE_EMPTY

            VIEW_STATE_LOADING.toInt() -> mViewState = VIEW_STATE_LOADING

            VIEW_STATE_UNKNOWN.toInt() -> mViewState = VIEW_STATE_UNKNOWN
            else -> mViewState = VIEW_STATE_UNKNOWN
        }

        a.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mContentView == null) throw IllegalArgumentException("Content view is not defined")
        setView(VIEW_STATE_UNKNOWN.toInt())
    }

    /* All of the addView methods have been overridden so that it can obtain the content view via XML
     It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
     any of the setViewForState methods to set views for their given ViewState accordingly */
    override fun addView(child: View?) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, index)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, index, params)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, params)
    }

    override fun addView(child: View, width: Int, height: Int) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, width, height)
    }

    override fun addViewInLayout(child: View, index: Int, params: ViewGroup.LayoutParams): Boolean {
        if (isValidContentView(child)) mContentView = child
        return super.addViewInLayout(child, index, params)
    }

    override fun addViewInLayout(child: View, index: Int, params: ViewGroup.LayoutParams, preventRequestLayout: Boolean): Boolean {
        if (isValidContentView(child)) mContentView = child
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

    /**
     * Returns the [View] associated with the [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param state The [com.kennyc.view.MultiStateView.ViewState] with to return the view for
     * @return The [View] associated with the [com.kennyc.view.MultiStateView.ViewState], null if no view is present
     */
    fun getView(@ViewState state: Int): View? {
        when (state) {
            VIEW_STATE_LOADING.toInt() -> return mLoadingView

            VIEW_STATE_CONTENT.toInt() -> return mContentView

            VIEW_STATE_EMPTY.toInt() -> return mEmptyView

            VIEW_STATE_ERROR.toInt() -> return mErrorView

            else -> return null
        }
    }

    /**
     * Returns the current [com.kennyc.view.MultiStateView.ViewState]
     *
     * @return
     */
    @ViewState
    fun getViewState(): Int {
        return mViewState.toInt()
    }

    /**
     * Sets the current [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param state The [com.kennyc.view.MultiStateView.ViewState] to set [MultiStateView] to
     */
    fun setViewState(@ViewState state: Int) {
        if (state != mViewState.toInt()) {
            val previous = mViewState
            mViewState = state.toLong()
            setView(previous.toInt())
            if (mListener != null) mListener!!.onStateChanged(mViewState.toInt())
        }
    }

    /**
     * Shows the [View] based on the [com.kennyc.view.MultiStateView.ViewState]
     */
    private fun setView(@ViewState previousState: Int) {
        when (mViewState) {
            VIEW_STATE_LOADING -> {
                if (mLoadingView == null) {
                    throw NullPointerException("Loading View")
                }

                if (mContentView != null) mContentView!!.visibility = View.GONE
                if (mErrorView != null) mErrorView!!.visibility = View.GONE
                if (mEmptyView != null) mEmptyView!!.visibility = View.GONE

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mLoadingView!!.visibility = View.VISIBLE
                }
            }

            VIEW_STATE_EMPTY -> {
                if (mEmptyView == null) {
                    throw NullPointerException("Empty View")
                }


                if (mLoadingView != null) mLoadingView!!.visibility = View.GONE
                if (mErrorView != null) mErrorView!!.visibility = View.GONE
                if (mContentView != null) mContentView!!.visibility = View.GONE

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mEmptyView!!.visibility = View.VISIBLE
                }
            }

            VIEW_STATE_ERROR -> {
                if (mErrorView == null) {
                    throw NullPointerException("Error View")
                }


                if (mLoadingView != null) mLoadingView!!.visibility = View.GONE
                if (mContentView != null) mContentView!!.visibility = View.GONE
                if (mEmptyView != null) mEmptyView!!.visibility = View.GONE

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mErrorView!!.visibility = View.VISIBLE
                }
            }

            VIEW_STATE_CONTENT -> {
                if (mContentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw NullPointerException("Content View")
                }


                if (mLoadingView != null) mLoadingView!!.visibility = View.GONE
                if (mErrorView != null) mErrorView!!.visibility = View.GONE
                if (mEmptyView != null) mEmptyView!!.visibility = View.GONE

                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mContentView!!.visibility = View.VISIBLE
                }
            }
            else -> {
                if (mContentView == null) {
                    throw NullPointerException("Content View")
                }
                if (mLoadingView != null) mLoadingView!!.visibility = View.GONE
                if (mErrorView != null) mErrorView!!.visibility = View.GONE
                if (mEmptyView != null) mEmptyView!!.visibility = View.GONE
                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mContentView!!.visibility = View.VISIBLE
                }
            }
        }
    }

    /**
     * Checks if the given [View] is valid for the Content View
     *
     * @param view The [View] to check
     * @return
     */
    private fun isValidContentView(view: View?): Boolean {
        return if (mContentView != null && mContentView !== view) {
            false
        } else view !== mLoadingView && view !== mErrorView && view !== mEmptyView

    }

    /**
     * Sets the view for the given view state
     *
     * @param view          The [View] to use
     * @param state         The [com.kennyc.view.MultiStateView.ViewState]to set
     * @param switchToState If the [com.kennyc.view.MultiStateView.ViewState] should be switched to
     */
    fun setViewForState(view: View, @ViewState state: Int, switchToState: Boolean) {
        when (state) {
            VIEW_STATE_LOADING.toInt() -> {
                if (mLoadingView != null) removeView(mLoadingView)
                mLoadingView = view
                addView(mLoadingView)
            }

            VIEW_STATE_EMPTY.toInt() -> {
                if (mEmptyView != null) removeView(mEmptyView)
                mEmptyView = view
                addView(mEmptyView)
            }

            VIEW_STATE_ERROR.toInt() -> {
                if (mErrorView != null) removeView(mErrorView)
                mErrorView = view
                addView(mErrorView)
            }

            VIEW_STATE_CONTENT.toInt() -> {
                if (mContentView != null) removeView(mContentView)
                mContentView = view
                addView(mContentView)
            }
        }

        setView(VIEW_STATE_UNKNOWN.toInt())
        if (switchToState) setViewState(state)
    }

    /**
     * Sets the [View] for the given [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param view  The [View] to use
     * @param state The [com.kennyc.view.MultiStateView.ViewState] to set
     */
    fun setViewForState(view: View, @ViewState state: Int) {
        setViewForState(view, state, false)
    }

    /**
     * Sets the [View] for the given [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param layoutRes     Layout resource id
     * @param state         The [com.kennyc.view.MultiStateView.ViewState] to set
     * @param switchToState If the [com.kennyc.view.MultiStateView.ViewState] should be switched to
     */
    fun setViewForState(@LayoutRes layoutRes: Int, @ViewState state: Int, switchToState: Boolean) {
        if (mInflater == null) mInflater = LayoutInflater.from(context)
        val view = mInflater!!.inflate(layoutRes, this, false)
        setViewForState(view, state, switchToState)
    }

    /**
     * Sets the [View] for the given [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param layoutRes Layout resource id
     * @param state     The [View] state to set
     */
    fun setViewForState(@LayoutRes layoutRes: Int, @ViewState state: Int) {
        setViewForState(layoutRes, state, false)
    }

    /**
     * Sets whether an animate will occur when changing between [ViewState]
     *
     * @param animate
     */
    fun setAnimateLayoutChanges(animate: Boolean) {
        mAnimateViewChanges = animate
    }

    /**
     * Sets the [StateListener] for the view
     *
     * @param listener The [StateListener] that will receive callbacks
     */
    fun setStateListener(listener: StateListener) {
        mListener = listener
    }

    /**
     * Animates the layout changes between [ViewState]
     *
     * @param previousView The view that it was currently on
     */
    private fun animateLayoutChange(previousView: View?) {
        if (previousView == null) {
            getView(mViewState.toInt())!!.visibility = View.VISIBLE
            return
        }

        previousView.visibility = View.VISIBLE
        val anim = ObjectAnimator.ofFloat(previousView, "alpha", 1.0f, 0.0f).setDuration(250L)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                previousView.visibility = View.GONE
                getView(mViewState.toInt())!!.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(getView(mViewState.toInt()), "alpha", 0.0f, 1.0f).setDuration(250L).start()
            }
        })
        anim.start()
    }

    interface StateListener {
        /**
         * Callback for when the [ViewState] has changed
         *
         * @param viewState The [ViewState] that was switched to
         */
        fun onStateChanged(@ViewState viewState: Int)
    }
}