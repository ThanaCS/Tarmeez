package com.thanaa.tarmeezapp

import android.content.Context
import com.google.gson.Gson
import com.thanaa.tarmeezapp.data.User

class PreferencesProvider(context: Context) {

    private val sharedPrefrences = context.getSharedPreferences("myPreferences", 0)

    fun putUser(key:String, user: User){
        val prefsEditor = sharedPrefrences.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("MyObject", json)
        prefsEditor.commit()
    }

    fun getUser(key:String): User {
        val gson = Gson()
        val json = sharedPrefrences.getString("MyObject", "")
        val obj = gson.fromJson(json, User::class.java)
        return obj
    }

}