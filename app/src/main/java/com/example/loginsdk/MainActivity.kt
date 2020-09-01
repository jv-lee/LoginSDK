package com.example.loginsdk

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_CODE = 0x001
    private val FACEBOOK_SIGN_CODE = 0x002
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //google登录
        findViewById<Button>(R.id.btn_google_login).setOnClickListener {
            //创建登录凭证申请
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_service_client_id))
                .requestEmail()
                .build()
            val client = GoogleSignIn.getClient(this, gso)
            val account = GoogleSignIn.getLastSignedInAccount(this)
            //验证是否授权
            if (account != null) {
                toast("已授权:" + account.email)
                return@setOnClickListener
            }
            //发起登录请求
            val signInIntent = client.signInIntent
            startActivityForResult(signInIntent, GOOGLE_SIGN_CODE)
        }

        //facebook登录
        val facebookButton = LoginButton(this)
        callbackManager = CallbackManager.Factory.create()
        facebookButton.setReadPermissions("email")
        facebookButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                toast(result?.accessToken?.token!!)
            }

            override fun onCancel() {
                toast("cancel")
            }

            override fun onError(error: FacebookException?) {
                toast("error -> $error")
                error?.printStackTrace()
            }

        })

        findViewById<Button>(R.id.btn_facebook_login).setOnClickListener {
            val token = checkFacebookToken()
            if (token == null) {
                facebookButton.performClick()
            } else {
                toast("账户已登录：$token")
            }
        }
        findViewById<Button>(R.id.btn_facebook_out).setOnClickListener {
            LoginManager.getInstance().logOut()
        }

        key()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //设置Facebook登录回调
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        //处理Google登录请求回调
        if (requestCode == GOOGLE_SIGN_CODE) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                toast("account:" + account?.email)
            } catch (e: Exception) {
                e.printStackTrace()
                toast("获取结果异常")
            }
        }
    }

    /**
     * 检测facebook是否成功登录
     */
    private fun checkFacebookToken(): String? {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLogin = accessToken != null && !accessToken.isExpired
        return if (isLogin) {
            accessToken.token
        } else {
            null
        }
    }

    private fun key() {
        try {
            val info = packageManager.getPackageInfo(
                "com.lee.login",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }
}