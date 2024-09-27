package com.maranguit.menu

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.title = "My Menu"
        supportActionBar?.subtitle = "Maranguit"

        // Set the listener for the menu button to show the PopupMenu
        toolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        if (savedInstanceState == null) {
            goToDefaultFragment()
        }
    }

    // Inflate the menu without using MenuBuilder directly
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_go_to_fragment -> {
                goToFirstMenuAction()
                true
            }
            R.id.action_show_dialog -> {
                showDialogFragment()
                true
            }
            R.id.action_exit -> {
                performThirdMenuAction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialogFragment() {
        val dialogFragment = MyDialogFragment()
        dialogFragment.show(supportFragmentManager, "MyDialogFragment")
    }

    // Set methods to public if used in other classes
    fun goToFirstMenuAction() {
        navigateToFragment(AnotherFragment())
    }

    fun goToDefaultFragment() {
        navigateToFragment(DefaultFragment())
    }

    fun performThirdMenuAction() {
        finish()
    }

    private fun navigateToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
