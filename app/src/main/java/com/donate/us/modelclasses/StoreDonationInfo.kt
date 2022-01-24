package com.donate.us.modelclasses

class StoreDonationInfo(
    private var userName: String,
    private var rice: String,
    private var egg: String,
    private var vegetable: String,
    private var chicken: String,
    private var redMeat: String,
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

    fun getRice(): String {
        return rice
    }

    fun setRice(rice: String?) {
        this.rice = rice!!
    }

    fun getEgg(): String {
        return egg
    }

    fun setEgg(egg: String?) {
        this.egg = egg!!
    }

    fun getVegetable(): String {
        return vegetable
    }

    fun setVegetable(vegetable: String?) {
        this.vegetable = vegetable!!
    }

    fun getChicken(): String {
        return chicken
    }

    fun setChicken(chicken: String?) {
        this.chicken = chicken!!
    }

    fun getRedMeat(): String {
        return redMeat
    }

    fun setRedMeat(redMeat: String?) {
        this.redMeat = redMeat!!
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
