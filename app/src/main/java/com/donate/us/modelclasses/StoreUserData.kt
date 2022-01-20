package com.donate.us.modelclasses

class StoreUserData(
    private var userName: String,
    private var userEmail: String,
    private var userPhone: String,
    private var userAddress: String
    ){

    fun getUserName(): String {
        return userName
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun getUserEmail(): String {
        return userEmail
    }

    fun setUserEmail(userEmail: String?) {
        this.userEmail = userEmail!!
    }

    fun getUserPhone(): String {
        return userPhone
    }

    fun setUserPhone(userPhone: String?) {
        this.userPhone = userPhone!!
    }

    fun getUserAddress(): String {
        return userAddress
    }

    fun setUserAddress(userAddress: String?) {
        this.userAddress = userAddress!!
    }
}
