package com.student.techapp.data

class Users(
    val uid: String,
    val profileImage: String,
    val username: String,
    val usersurname: String,
    val usermiddlename: String,
    val birthday: String,
    val city: String,
    val about: String
){
    constructor():this("","","","","","","","")
}