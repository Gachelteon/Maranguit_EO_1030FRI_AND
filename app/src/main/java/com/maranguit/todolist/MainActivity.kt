package com.maranguit.todolist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    private var lastClickedPosition: Int = -1
    private val DOUBLE_CLICK_DELAY: Long = 300 // milliseconds
    private val clickHandler = Handler(Looper.getMainLooper())
    private var pendingClick: Runnable? = null

    private var editingTaskPosition: Int = -1
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        editText = findViewById(R.id.editText)

        adapter = TaskAdapter(this, tasks)
        listView.adapter = adapter

        editText.setOnEditorActionListener { _, _, _ ->
            addTask()
            true
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            handleItemClick(position)
        }
    }

    private fun handleItemClick(position: Int) {
        Log.d("MainActivity", "Item clicked at position: $position")

        if (position == lastClickedPosition && pendingClick != null) {
            clickHandler.removeCallbacks(pendingClick!!)
            pendingClick = null
            showEditDeleteDialog(position)
            Log.d("MainActivity", "Double click detected, showing dialog")
        } else {
            pendingClick = Runnable {
                adapter.toggleTaskCompletion(position)
                pendingClick = null
                Log.d("MainActivity", "Single click action performed")
            }
            clickHandler.postDelayed(pendingClick!!, DOUBLE_CLICK_DELAY)

            lastClickedPosition = position
        }
    }

    private fun addTask() {
        val taskText = editText.text.toString()
        if (taskText.isNotEmpty()) {
            tasks.add(Task(taskText, false, null))
            adapter.notifyDataSetChanged()
            editText.text.clear()
        }
    }

    private fun showEditDeleteDialog(position: Int) {
        val task = tasks[position]
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit or Delete Task")

        val options = arrayOf("Edit", "Delete")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showEditDialog(task, position)
                1 -> deleteTask(position)
            }
        }
        builder.show()
    }

    private fun showEditDialog(task: Task, position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Task")

        val dialogView = layoutInflater.inflate(R.layout.edit_task_dialog, null)
        val input: EditText = dialogView.findViewById(R.id.editTaskDescription)
        val imageButton: Button = dialogView.findViewById(R.id.selectImageButton)

        input.setText(task.description)

        imageButton.setOnClickListener {
            editingTaskPosition = position
            openGallery()
        }

        builder.setView(dialogView)

        builder.setPositiveButton("OK") { _, _ ->
            task.description = input.text.toString()
            adapter.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel", null)

        builder.show()
    }

    private fun deleteTask(position: Int) {
        tasks.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (editingTaskPosition != -1 && selectedImageUri != null) {
                tasks[editingTaskPosition].imageUri = selectedImageUri
                adapter.notifyDataSetChanged()
                editingTaskPosition = -1
            }
        }
    }
}


