package com.maranguit.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class TaskAdapter(private val context: Context, private val tasks: List<Task>) : BaseAdapter() {

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

        val task = getItem(position) as Task

        viewHolder.checkBox.isChecked = task.isCompleted
        viewHolder.textView.text = task.description
        task.imageUri?.let { uri ->
            viewHolder.imageView.setImageURI(uri)
        } ?: run {
            viewHolder.imageView.setImageResource(R.drawable.ic_task_image)
        }

        viewHolder.checkBox.setOnCheckedChangeListener(null)
        viewHolder.checkBox.isFocusable = false
        viewHolder.checkBox.isClickable = false

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
        val task = getItem(position) as Task
        task.isCompleted = !task.isCompleted
        notifyDataSetChanged()
    }
}