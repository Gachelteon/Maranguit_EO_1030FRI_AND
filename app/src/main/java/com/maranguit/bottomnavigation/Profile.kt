package com.maranguit.bottomnavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.maranguit.bottomnavigation.databinding.FragmentProfileBinding

class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Handle profile image edit
        binding.editImageButton.setOnClickListener {
            openGallery()
        }

        // Handle save button click
        binding.saveButton.setOnClickListener {
            saveProfile()
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            binding.profileImage.setImageURI(selectedImageUri)
        }
    }

    private fun saveProfile() {
        val fullName = binding.etFullName.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()

        val gender = when (binding.genderRadioGroup.checkedRadioButtonId) {
            R.id.radioMale -> "Male"
            R.id.radioFemale -> "Female"
            else -> "Other"
        }

        val hobbies = mutableListOf<String>()
        if (binding.cbReading.isChecked) hobbies.add("Reading")
        if (binding.cbTraveling.isChecked) hobbies.add("Traveling")
        if (binding.cbGaming.isChecked) hobbies.add("Gaming")

        // Saving profile info to SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("UserProfile", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("fullName", fullName)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.putString("gender", gender)
        editor.putStringSet("hobbies", hobbies.toSet())
        editor.apply()

        Toast.makeText(requireContext(), "Profile Saved", Toast.LENGTH_SHORT).show()
    }


}