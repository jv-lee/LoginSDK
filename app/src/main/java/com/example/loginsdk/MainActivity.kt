package com.example.loginsdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.login.library.SignInManager
import com.login.library.annotation.SignInType
import com.login.library.bean.AccountResponse
import com.login.library.core.SignInCallback
import com.login.library.tools.SignInTool


class MainActivity : AppCompatActivity(), SignInCallback {

    private val TAG = "SignInLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SignInManager.get().bindContext(this, this)

        //google登录
        findViewById<Button>(R.id.btn_google_login).setOnClickListener {
            SignInManager.get().signInByType(SignInType.GOOGLE)
        }

        //facebook登录
        findViewById<Button>(R.id.btn_facebook_login).setOnClickListener {
            SignInManager.get().signInByType(SignInType.FACEBOOK)
        }

        findViewById<Button>(R.id.btn_facebook_out).setOnClickListener {
            SignInManager.get().signOutByType(SignInType.FACEBOOK)
        }

    }

    override fun signInSuccess(type: Int, response: AccountResponse) {
        when (type) {
            SignInType.GOOGLE -> Log.i(TAG, "signInSuccess: google->$response")
            SignInType.FACEBOOK -> Log.i(TAG, "signInSuccess: facebook->$response")
        }

    }

    override fun signInFailed(type: Int, msg: String) {
        when (type) {
            SignInType.GOOGLE -> Log.i(TAG, "signInFailed: google->$msg")
            SignInType.FACEBOOK -> Log.i(TAG, "signInFailed: facebook->$msg")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        SignInTool.facebookForResult(
            supportFragmentManager.fragments,
            requestCode,
            resultCode,
            data
        )
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

}