package com.login.library.tools

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.login.library.fragment.IntentFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
object SignInTool {

    fun applyFragment(activity: FragmentActivity, fragment: Fragment?) {
        val fragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        fragment?.let {
            if (it.isAdded) {
                transaction.remove(it)
            }
            transaction.add(it, IntentFragment::class.java.simpleName)
            transaction.addToBackStack(IntentFragment::class.java.simpleName)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
    }

    /**
     * facebook登录扩充方法
     */
    fun facebookForResult(
        fragments: List<Fragment>,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        for (fragment in fragments) {
            if (fragment is IntentFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    /**
     * 打印应用KeyHash值
     */
    fun printKeyHash(packageManager: PackageManager, packageName: String) {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }
}