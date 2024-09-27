package com.maranguit.menu

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)
            builder.setTitle("Dialog Title")
                .setMessage("This is the message in the dialog.")

                .setNegativeButton("Close") { _, _ ->
                    dialog?.dismiss()
                }
                .setPositiveButton("Go back to Default") { _, _ ->
                    (activity as? MainActivity)?.goToDefaultFragment()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}