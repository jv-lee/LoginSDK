package com.login.library.core

import android.content.Intent
import com.login.library.bean.AccountResponse

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
interface SignInRequest<T> {
    fun requestSignIn()
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    )

    fun buildAccount(data: T?): AccountResponse
}