package com.student.techapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Request(
    val fromId: String,
    val name: String,
    val address: String,
    val phone: String,
    val service: String,
    val problem: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "")
}