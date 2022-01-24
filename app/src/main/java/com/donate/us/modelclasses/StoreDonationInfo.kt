package com.donate.us.modelclasses

class StoreDonationInfo(
    private var userName: String,
    private var foodTypes: String,
    private var quantityPeople: String,
    private var avatar: String,
    private var pickAdress: String,
    private var date: String,
    private var time: String
    ) {

    fun getUserName(): String {
        return userName
    }

    fun setUserName(userName: String?) {
        this.userName = userName!!
    }

    fun getFoodTypes(): String {
        return foodTypes
    }

    fun setFoodTypes(foodTypes: String?) {
        this.foodTypes = foodTypes!!
    }

    fun getQuantityPeople(): String {
        return quantityPeople
    }

    fun setQuantityPeople(quantityPeople: String?) {
        this.quantityPeople = quantityPeople!!
    }

    fun getAvatar(): String {
        return avatar
    }

    fun setAvatar(avatar: String?) {
        this.avatar = avatar!!
    }

    fun getPickAdress(): String {
        return pickAdress
    }

    fun setPickAdress(pickAdress: String?) {
        this.pickAdress = pickAdress!!
    }

    fun getDate(): String {
        return date
    }

    fun setDate(date: String?) {
        this.date = date!!
    }

    fun getTime(): String {
        return time
    }

    fun setTime(time: String?) {
        this.time = time!!
    }
}
