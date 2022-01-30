package com.donate.us.modelclasses

data class StoreDonationInfo(
    var userName: String ?= null,
    var rice: String ?= null,
    var egg: String ?= null,
    var vegetable: String ?= null,
    var chicken: String ?= null,
    var redMeat: String ?= null,
    var quantityPeople: String ?= null,
    var avatar: String ?= null,
    var pickAdress: String ?= null,
    var date: String ?= null,
    var time: String ?= null,
    var status: String ?= null
)
