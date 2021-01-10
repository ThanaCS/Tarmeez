package com.thanaa.tarmeezapp.data

data class Game(val gameId: String,
                val gameName: String,
                val question: String,
                val answer: String,
                val options: String,
                val quizType: String)