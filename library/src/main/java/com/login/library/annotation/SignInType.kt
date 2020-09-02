package com.login.library.annotation

import androidx.annotation.IntDef
import com.login.library.annotation.SignInType.Companion.FACEBOOK
import com.login.library.annotation.SignInType.Companion.GOOGLE

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
@IntDef(GOOGLE, FACEBOOK)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class SignInType {

    companion object {
        const val GOOGLE: Int = 0x001
        const val FACEBOOK: Int = 0x002
    }
}