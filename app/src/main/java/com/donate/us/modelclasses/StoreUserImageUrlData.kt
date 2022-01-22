package com.donate.us.modelclasses

class StoreUserImageUrlData (
    private var avatar: String
    ){

    fun getAvatar(): String {
        return avatar
    }

    fun setAvatar(avatar: String?) {
        this.avatar = avatar!!
    }
}
