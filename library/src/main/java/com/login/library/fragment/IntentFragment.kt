package com.login.library.fragment

import android.content.Intent
import androidx.fragment.app.Fragment
import com.login.library.SignInManager

/**
 * @author jv.lee
 * @date 2019/4/14
 */
class IntentFragment : Fragment() {
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        SignInManager.get().currentRequest?.run {
            try {
                onActivityResult(requestCode, resultCode, data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}