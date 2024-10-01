package com.maranguit.bottomnavigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.maranguit.bottomnavigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the default fragment to Profile
        replaceFragment(ToDoList())

        // Set Profile as the default selected item in the BottomNavigationView
        binding.bottomNavigationView.selectedItemId = R.id.list

        // Handle BottomNavigationView item clicks
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.calculator -> replaceFragment(Calculator())
                R.id.list -> replaceFragment(ToDoList())
                R.id.profile -> replaceFragment(Profile())
                else -> {
                    // No action needed
                }

            }
            true
        }
    }

    /**
     * Replaces the current fragment with the specified fragment
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
