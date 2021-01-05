package com.thanaa.tarmeezapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.thanaa.tarmeezapp.data.Content
import com.thanaa.tarmeezapp.data.Planet
import com.thanaa.tarmeezapp.data.Quiz
import com.thanaa.tarmeezapp.data.Section

class MainActivity : AppCompatActivity() {
    lateinit var fab: FloatingActionButton
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var bottomAppBar: BottomAppBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        fab = findViewById(R.id.fab)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomAppBar = findViewById(R.id.bottomAppBar)
        val navController: NavController =
            Navigation.findNavController(this, R.id.fragment_container)
        bottomNavigationView.background = null
        fab.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        findNavController(R.id.fragment_container)

//        val ref = FirebaseDatabase.getInstance().getReference("Planet")
//        val planetId = ref.push().key
//        val quiz1 = Quiz("question","answer","options")
//        val quiz2 = Quiz("question","answer","options")
//        val quizes = listOf<Quiz>(quiz1,quiz2)
//        val content = Content("title","descation",0,quizes)
//        val section = Section("مقدمة", content)
//        val section1 = Section("مقدمة", content)
//        val section2 = Section("مقدمة", content)
//        val sections = listOf<Section>(section,section1,section2)
//        val planet = Planet(planetId, "المتغيرات",sections)
//        if (planetId != null) {
//            ref.child(planetId).setValue(planet)
//        }


    }
}