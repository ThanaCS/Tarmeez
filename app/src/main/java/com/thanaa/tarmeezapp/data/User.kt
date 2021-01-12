package com.thanaa.tarmeezapp.data

data class User(val userId: String,
                val username: String,
                val age: String,
                val gender: String,
                val email: String,
                var score: Int,
                val flag: String)