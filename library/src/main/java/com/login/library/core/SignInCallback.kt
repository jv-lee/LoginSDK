package com.login.library.core

import com.login.library.annotation.SignInType
import com.login.library.bean.AccountResponse

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
interface SignInCallback {
    fun signInSuccess(@SignInType type: Int, response: AccountResponse)
    fun signInFailed(@SignInType type: Int, msg: String)
}