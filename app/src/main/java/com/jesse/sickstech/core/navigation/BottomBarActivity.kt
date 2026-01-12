package com.jesse.sickstech.core.navigation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jesse.sickstech.R
import com.jesse.sickstech.databinding.ActivityBottomBarBinding

class BottomBarActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityBottomBarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment não encontrado no layout com id R.id.nav_host")
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}