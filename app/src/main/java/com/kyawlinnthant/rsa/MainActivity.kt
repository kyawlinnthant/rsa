package com.kyawlinnthant.rsa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(app_toolbar)
        setupNavigation()
    }

    private fun setupNavigation(){
        val host = supportFragmentManager.findFragmentById(R.id.host) as NavHostFragment
        val controller = host.navController

        app_toolbar.setupWithNavController(controller)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
