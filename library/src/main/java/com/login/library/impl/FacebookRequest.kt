package com.login.library.impl

import android.content.Intent
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.login.library.annotation.SignInType
import com.login.library.bean.AccountResponse
import com.login.library.core.SignInCallback
import com.login.library.core.SignInRequest

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
internal class FacebookRequest(
    private val fragment: Fragment,
    private val signInCallback: SignInCallback
) :
    SignInRequest<String>(fragment, signInCallback) {

    private var callbackManager: CallbackManager? = null

    override fun requestSignIn() {
        //校验令牌是否存在且是否过期
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            buildAccount(accessToken.token)
            return
        }
        //facebook登录
        val facebookButton = LoginButton(fragment.requireContext())
        callbackManager = CallbackManager.Factory.create()
        facebookButton.setReadPermissions("email")
        facebookButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                buildAccount(result?.accessToken?.token)
            }

            override fun onCancel() {
                signInCallback.signInFailed(SignInType.FACEBOOK, "cancel")
            }

            override fun onError(error: FacebookException?) {
                signInCallback.signInFailed(SignInType.FACEBOOK, error?.message ?: "error ???")
                error?.printStackTrace()
            }

        })
        facebookButton.performClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun buildAccount(data: String?): AccountResponse {
        var account: AccountResponse? = null
        account = AccountResponse("123", data, "userName", "url", null)
        signInCallback.signInSuccess(
            SignInType.FACEBOOK,
            account
        )
        return account
    }

    override fun isExpired(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    override fun signOut() {
        LoginManager.getInstance().logOut()
    }

}