package com.maranguit.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment

class HeadlineListFragment : Fragment() {

    private var listener: OnHeadlineSelectedListener? = null

    // Interface to communicate with MainActivity
    interface OnHeadlineSelectedListener {
        fun onHeadlineSelected(position: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnHeadlineSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_headline_list, container, false)

        val headlines = listOf(
            "Breaking News: Market Hits Record High",
            "Sports Update: Local Team Wins Championship",
            "Weather Alert: Heavy Rain Expected Tomorrow",
            "Technology: New Smartphone Released",
            "Health Tips: 10 Ways to Stay Healthy"
        )

        val listView = view.findViewById<android.widget.ListView>(R.id.headline_list)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, headlines)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            listener?.onHeadlineSelected(position)
            // Update the ViewModel with the selected position
        }

        return view
    }
}
