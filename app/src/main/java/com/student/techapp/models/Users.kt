package com.student.techapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    val uid: String,
    val profileImage: String,
    val username: String,
    val usersurname: String,
    val address: String,
    val birthday: String,
    val city: String,
    val about: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "")
}