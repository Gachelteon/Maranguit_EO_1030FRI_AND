package com.maranguit.bottomnavigation

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class TDListAdapter(private val context: Context, private val tasks: List<TDList>) : BaseAdapter() {

    override fun getCount(): Int = tasks.size

    override fun getItem(position: Int): Any = tasks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.itemCheckBox),
                view.findViewById(R.id.itemText),
                view.findViewById(R.id.dis_pic)
            )
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val task = getItem(position) as TDList

        // Set checkbox and text view states
        viewHolder.checkBox.isChecked = task.isCompleted
        viewHolder.textView.text = task.description

        // Apply strike-through when the task is completed
        if (task.isCompleted) {
            viewHolder.textView.paintFlags = viewHolder.textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            viewHolder.textView.paintFlags = viewHolder.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Set image if available
        task.imageUri?.let { uri ->
            viewHolder.imageView.setImageURI(uri)
        } ?: run {
            viewHolder.imageView.setImageResource(R.drawable.ic_task_image)
        }

        // Prevent default checkbox behavior to avoid unwanted callbacks
        viewHolder.checkBox.setOnCheckedChangeListener(null)
        viewHolder.checkBox.isFocusable = false
        viewHolder.checkBox.isClickable = false

        // Disable click and focus states on the list item
        view.isClickable = false
        view.isFocusable = false

        return view
    }

    private data class ViewHolder(
        val checkBox: CheckBox,
        val textView: TextView,
        val imageView: ImageView
    )

    fun toggleTaskCompletion(position: Int) {
        val task = getItem(position) as TDList
        task.isCompleted = !task.isCompleted
        notifyDataSetChanged()
    }
}
