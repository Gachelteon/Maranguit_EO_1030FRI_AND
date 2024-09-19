package com.maranguit.news

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), HeadlineListFragment.OnHeadlineSelectedListener {

    private var currentHeadlinePosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentHeadlinePosition = savedInstanceState.getInt("selected_headline_position", -1)
        }

        setupFragments()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected_headline_position", currentHeadlinePosition)
    }

    private fun setupFragments() {
        val isLandscape = findViewById<FrameLayout>(R.id.fragment_headline_list) != null

        if (isLandscape) {
            // Landscape mode
            if (supportFragmentManager.findFragmentById(R.id.fragment_headline_list) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_headline_list, HeadlineListFragment())
                    .commit()
            }
            if (supportFragmentManager.findFragmentById(R.id.fragment_news_content) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_news_content, NewsContentFragment())
                    .commit()
            }
        } else {
            // Portrait mode
            if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HeadlineListFragment())
                    .commit()
            }
        }

        // Restore selected headline if any
        if (currentHeadlinePosition != -1) {
            onHeadlineSelected(currentHeadlinePosition)
        }
    }

    override fun onHeadlineSelected(position: Int) {
        currentHeadlinePosition = position
        val newsContentFragment = NewsContentFragment().apply {
            arguments = Bundle().apply {
                putInt("selected_headline_position", position)
            }
        }

        if (findViewById<FrameLayout>(R.id.fragment_news_content) != null) {
            // Landscape mode: update the existing news content fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_news_content, newsContentFragment)
                .commit()
        } else {
            // Portrait mode: replace the fragment container with the news content fragment and add to back stack
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newsContentFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}