package com.donate.us.modelclasses

data class StoreUserData(
    var userName: String ?= null,
    var userEmail: String ?= null,
    var userPhone: String ?= null,
    var userAddress: String ?= null
)
