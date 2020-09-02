package com.login.library.core

import android.content.Intent
import androidx.fragment.app.Fragment
import com.login.library.bean.AccountResponse

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
abstract class SignInRequest<T> constructor(
    private val fragment: Fragment,
    private val signInCallback: SignInCallback
) {
    abstract fun requestSignIn()
    abstract fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    )

    abstract fun buildAccount(data: T?): AccountResponse

    abstract fun isExpired(): Boolean
    abstract fun signOut()
}