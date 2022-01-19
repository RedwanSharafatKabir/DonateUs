package com.donate.us.ModelClasses

class StoreUserData {
    private var userName: String
    private var userEmail: String
    private var userPhone: String

    constructor(userName: String, userEmail: String, userPhone: String) {
        this.userName = userName
        this.userEmail = userEmail
        this.userPhone = userPhone
    }

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun getUserEmail(): String? {
        return userEmail
    }

    fun setUserEmail(userEmail: String?) {
        this.userEmail = userEmail!!
    }

    fun getUserPhone(): String? {
        return userPhone
    }

    fun setUserPhone(userPhone: String?) {
        this.userPhone = userPhone!!
    }
}
