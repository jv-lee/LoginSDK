package com.login.library.factory

import androidx.fragment.app.Fragment
import com.login.library.annotation.SignInType
import com.login.library.core.SignInCallback
import com.login.library.core.SignInRequest
import com.login.library.impl.FacebookRequest
import com.login.library.impl.GoogleRequest

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
internal object SignInFactory {
    fun createRequest(
        @SignInType type: Int,
        fragment: Fragment?,
        signInCallback: SignInCallback?
    ): SignInRequest<*>? {
        if (fragment == null) {
            return null
        }
        if (signInCallback == null) {
            return null
        }
        return when (type) {
            SignInType.GOOGLE -> GoogleRequest(fragment, signInCallback)
            SignInType.FACEBOOK -> FacebookRequest(fragment, signInCallback)
            else -> null
        }
    }
}