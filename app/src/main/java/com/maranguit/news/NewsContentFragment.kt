package com.maranguit.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class NewsContentFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true // Retain this fragment instance during configuration changes
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_content, container, false)
        val newsContentTextView = view.findViewById<TextView>(R.id.news_content)

        // Get the position of the selected headline
        val position = arguments?.getInt("selected_headline_position") ?: 0

        // Example news contents
        val newsContents = listOf(
            "Market hits a record high today, with stocks soaring across all sectors...",
            "The local team clinched the championship title in a thrilling match...",
            "A heavy rain warning has been issued for tomorrow. Citizens are advised to stay indoors...",
            "The latest smartphone model features cutting-edge technology and an innovative design...",
            "Health experts share the top 10 ways to stay healthy in the current season..."
        )

        // Display the corresponding news content
        newsContentTextView.text = newsContents[position]

        return view
    }
}
