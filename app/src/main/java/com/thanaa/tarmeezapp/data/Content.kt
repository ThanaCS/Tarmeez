package com.thanaa.tarmeezapp.data

data class Content(val contentTitle: String,
                   val contentDescription: String,
                   val flag: Int,
                   val quiz: List<Quiz?>)