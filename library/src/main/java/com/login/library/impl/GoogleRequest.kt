package com.login.library.impl

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.login.library.R
import com.login.library.annotation.SignInType
import com.login.library.bean.AccountResponse
import com.login.library.constants.Constants.GOOGLE_SIGN_CODE
import com.login.library.core.SignInCallback
import com.login.library.core.SignInRequest

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
internal class GoogleRequest(
    private val fragment: Fragment,
    private val signInCallback: SignInCallback
) :
    SignInRequest<GoogleSignInAccount>(fragment, signInCallback) {

    override fun requestSignIn() {
        //校验令牌是否存在且是否过期
        val account = GoogleSignIn.getLastSignedInAccount(fragment.requireContext())
        if (account != null && !account.isExpired) {
            signInCallback.signInSuccess(SignInType.GOOGLE, buildAccount(account))
            return
        }
        //创建登录凭证申请
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(fragment.getString(R.string.google_service_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(fragment.requireContext(), gso)
        //发起登录请求
        val signInIntent = client.signInIntent
        fragment.startActivityForResult(signInIntent, GOOGLE_SIGN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //处理Google登录请求回调
        if (requestCode == GOOGLE_SIGN_CODE) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                signInCallback.signInSuccess(
                    SignInType.GOOGLE,
                    buildAccount(account)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                signInCallback.signInFailed(SignInType.GOOGLE, e.message ?: "error ???")
            }
        }
    }

    override fun buildAccount(data: GoogleSignInAccount?): AccountResponse {
        return AccountResponse(
            data?.id,
            data?.idToken,
            data?.displayName,
            data?.photoUrl.toString(),
            data
        )
    }

    override fun isExpired(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(fragment.requireContext())
        return account != null && !account.isExpired
    }

    override fun signOut() {

    }

}