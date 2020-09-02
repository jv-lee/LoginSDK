package com.login.library

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.login.library.annotation.SignInType
import com.login.library.fragment.IntentFragment
import com.login.library.core.SignInCallback
import com.login.library.core.SignInRequest
import com.login.library.factory.SignInFactory
import com.login.library.tools.SignInTool

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
class SignInManager : LifecycleObserver {

    companion object {
        @Volatile
        private var instance: SignInManager? = null

        @JvmStatic
        fun get() = instance ?: synchronized(this) {
            instance
                ?: SignInManager()
                    .also { instance = it }
        }
    }

    private var fragment: IntentFragment? = null
    var signInCallback: SignInCallback? = null
    var activity: FragmentActivity? = null
    var currentRequest: SignInRequest<*>? = null

    fun bindContext(activity: FragmentActivity, signInCallback: SignInCallback) {
        this.signInCallback = signInCallback
        this.activity = activity
        this.fragment = fragment ?: IntentFragment()
        //设置生命周期绑定
        activity.lifecycle.addObserver(this)
        //设置空fragment至目标Activity
        SignInTool.applyFragment(activity, fragment)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun signInByType(@SignInType type: Int) {
        if (activity?.isDestroyed!!) return
        currentRequest = SignInFactory.createRequest(type, fragment, signInCallback)
        currentRequest?.run { requestSignIn() }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun signOutByType(@SignInType type: Int) {
        if (activity?.isDestroyed!!) return
        currentRequest = SignInFactory.createRequest(type, fragment, signInCallback)
        currentRequest?.run { signOut() }
    }

    /**
     * false 为有效 / true 为失效
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun isExpiredByType(@SignInType type: Int): Boolean {
        if (activity?.isDestroyed!!) return true
        currentRequest = SignInFactory.createRequest(type, fragment, signInCallback)
        currentRequest?.run { return isExpired() }
        return true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        activity = null
        currentRequest = null
        signInCallback = null
        fragment = null
    }

}