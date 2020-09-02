package com.login.library.bean

/**
 * @author jv.lee
 * @date 2020/9/2
 * @description
 */
data class AccountResponse(
    val userId: String?,
    val token: String?,
    val userName: String?,
    val email: String?,
    val photoUrl: String?,
    val typeData: Any?
)