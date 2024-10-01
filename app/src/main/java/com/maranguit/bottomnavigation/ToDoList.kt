package com.maranguit.bottomnavigation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment

class ToDoList : Fragment() {

    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private lateinit var adapter: TDListAdapter
    private val tasks = mutableListOf<TDList>()

    private var lastClickedPosition: Int = -1
    private val DOUBLE_CLICK_DELAY: Long = 300 // milliseconds
    private val clickHandler = Handler(Looper.getMainLooper())
    private var pendingClick: Runnable? = null

    private var editingTaskPosition: Int = -1
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        // Initialize views
        listView = view.findViewById(R.id.listView)
        editText = view.findViewById(R.id.editText)

        // Set up the adapter
        adapter = TDListAdapter(requireContext(), tasks)
        listView.adapter = adapter

        // Set editor action listener to add tasks
        editText.setOnEditorActionListener { _, _, _ ->
            addTask()
            true
        }

        // Set item click listener to handle task clicks
        listView.setOnItemClickListener { _, _, position, _ ->
            handleItemClick(position)
        }

        return view
    }

    private fun handleItemClick(position: Int) {
        Log.d("TaskListFragment", "Item clicked at position: $position")

        if (position == lastClickedPosition && pendingClick != null) {
            clickHandler.removeCallbacks(pendingClick!!)
            pendingClick = null
            showEditDeleteDialog(position)
            Log.d("TaskListFragment", "Double click detected, showing dialog")
        } else {
            pendingClick = Runnable {
                adapter.toggleTaskCompletion(position)
                pendingClick = null
                Log.d("TaskListFragment", "Single click action performed")
            }
            clickHandler.postDelayed(pendingClick!!, DOUBLE_CLICK_DELAY)

            lastClickedPosition = position
        }
    }

    private fun addTask() {
        val taskText = editText.text.toString()
        if (taskText.isNotEmpty()) {
            tasks.add(TDList(taskText, false, null))
            adapter.notifyDataSetChanged()
            editText.text.clear()
        }
    }

    private fun showEditDeleteDialog(position: Int) {
        val task = tasks[position]
        val builder = AlertDialog.Builder(requireContext())
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

    private fun showEditDialog(task: TDList, position: Int) {
        val builder = AlertDialog.Builder(requireContext())
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
