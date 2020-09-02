package com.login.library.impl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.login.library.annotation.SignInType
import com.login.library.bean.AccountResponse
import com.login.library.core.SignInCallback
import com.login.library.core.SignInRequest
import org.json.JSONObject
import java.lang.Exception

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
internal class FacebookRequest(
    private val fragment: Fragment,
    private val signInCallback: SignInCallback
) :
    SignInRequest<AccessToken>(fragment, signInCallback) {

    private var callbackManager: CallbackManager? = null

    override fun requestSignIn() {
        //校验令牌是否存在且是否过期
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            buildAccount(accessToken)
            return
        }
        //facebook登录
        val facebookButton = LoginButton(fragment.requireContext())
        callbackManager = CallbackManager.Factory.create()
        facebookButton.setReadPermissions("email")
        facebookButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                buildAccount(result?.accessToken)
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

    override fun buildAccount(data: AccessToken?): AccountResponse {
        val params = Bundle()
        val request = GraphRequest.newMeRequest(
            data
        ) { `object`, response ->
            try {
                val account = AccountResponse(
                    `object`.getString("id"),
                    data?.token,
                    "${`object`.getString("first_name")}${`object`.getString("last_name")}",
                    `object`.getString("email"),
                    "${Profile.getCurrentProfile().getProfilePictureUri(200, 200)}",
                    null
                )

                signInCallback.signInSuccess(
                    SignInType.FACEBOOK,
                    account
                )
            } catch (e: Exception) {
                e.printStackTrace()
                signInCallback.signInFailed(
                    SignInType.FACEBOOK,
                    e.message ?: "buildFacebookData error."
                )
            }
        }
        params.putString("fields", "id,email,first_name,last_name,gender")
        request.parameters = params
        request.executeAsync()
        return AccountResponse("", data?.token, "", "", "", null)
    }

    override fun isExpired(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    override fun signOut() {
        LoginManager.getInstance().logOut()
    }

}