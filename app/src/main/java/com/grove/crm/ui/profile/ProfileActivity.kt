package com.grove.crm.ui.profile

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Patterns
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.grove.crm.BuildConfig
import com.grove.crm.Constants
import com.grove.crm.R
import com.grove.crm.ui.main.MainActivity
import com.grove.crm.ui.policy.PolicyActivity
import com.grove.crm.utils.AccountManager
import com.grove.crm.utils.BaseActivity
import com.grove.crm.utils.CustomProgressDialog
import com.grove.crm.utils.Logger
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : BaseActivity() {

    companion object {
        const val CALLING_ACTIVITY = "calling_activity"
    }
    private var dateOfBirth: Date? = null
    lateinit var profileViewModel: ProfileViewModel
    private var name: String? = null
    private var birthday: String? = null
    private var email: String? = null
    private var currentPhotoPath: String? = null
    private var parentActivity: String? = null

    private var requestImageLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request code
            val data = result.data
            data?.data?.let { selectedImage ->
                uploadImage(getPath(selectedImage))
            }
        }
    }

    private var requestCameraLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {

            currentPhotoPath?.let { selectedImage ->

                Logger.d(selectedImage)
                uploadImage(selectedImage)
            }
        }
    }

    private fun uploadImage(filePath: String) {
        try {

            val fileExt = filePath.substring(filePath.lastIndexOf(".") + 1)
            if (fileExt == "img" || fileExt == "jpg" || fileExt == "jpeg" || fileExt == "gif" || fileExt == "png") {
                CustomProgressDialog.showProgressDialog(this)
                profileViewModel.uploadAvatar(filePath)
                    ?.observe(this, Observer { isSuccess ->
                        CustomProgressDialog.dismissProgressDialog()
                        if (isSuccess) {
                            showAvatar(filePath)
                        }
                    })
            } else {
                //NOT IN REQUIRED FORMAT
            }
        } catch (e: FileNotFoundException) {
            Logger.e(e)
        }
    }

    private val requestAccessImagePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                chooseImage()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                Logger.e("${it.key} = ${it.value}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setTitle(R.string.profile_info)

        parentActivity = intent.getStringExtra(CALLING_ACTIVITY)
        if (parentActivity == PolicyActivity::class.java.simpleName) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        btnDone.setOnClickListener {
            if (!isValidInput()) {
                return@setOnClickListener
            }
            CustomProgressDialog.showProgressDialog(this)
            profileViewModel.updateAccount(name, birthday, email)
                ?.observe(this, Observer { isSuccess ->
                    CustomProgressDialog.dismissProgressDialog()
                    if (isSuccess) {
                        if (parentActivity == PolicyActivity::class.java.simpleName) {
                            openMainActivity()
                        }
                        finish()
                    }
                })
        }

        edtFullName.addTextChangedListener {
            name = it?.toString()
        }

        edtBirthday.addTextChangedListener {
            birthday = it?.toString()
        }

        edtEmail.addTextChangedListener {
            email = it?.toString()
        }

        edtBirthday.setOnClickListener {
            showDatePicker()
        }

        ivAvatar.setOnClickListener {
            showChooseImageDialog()
        }

        AccountManager.account?.apply {
            showAvatar(urlAvatar)
            edtFullName.setText(fullName)
            edtBirthday.setText(dob)
            edtPhone.setText(phoneNumber)
            edtEmail.setText(email)

            if (!birthday.isNullOrEmpty()) {
                val format = SimpleDateFormat(Constants.dateFormat)
                try {
                    dateOfBirth = format.parse(birthday!!)
                } catch (e: ParseException) {
                    Logger.e(e)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun getPath(uri: Uri): String {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)

        cursor?.apply {
            if (moveToFirst()) {
                val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = getString(columnIndex)
            }
            close()
        }
        return res ?: ""
    }

    private fun chooseImage() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        requestImageLauncher.launch(photoPickerIntent)

    }

    private fun openCamera() {
        try {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        Logger.e(ex)
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID ,
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        requestCameraLauncher.launch(takePictureIntent)
                    }
                }
            }

        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        dateOfBirth?.let {
            calendar.time = it
        }

        val mYear = calendar[Calendar.YEAR]
        val mMonth = calendar[Calendar.MONTH]
        val mDay = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            this,
            myDateListener,
            mYear,
            mMonth,
            mDay
        )
        val minCalendar = Calendar.getInstance()
        minCalendar[Calendar.YEAR] = minCalendar[Calendar.YEAR] - 100
        datePickerDialog.datePicker.minDate = minCalendar.timeInMillis

        val maxCalendar = Calendar.getInstance()
        maxCalendar[Calendar.YEAR] = maxCalendar[Calendar.YEAR] - 5
        datePickerDialog.datePicker.maxDate = maxCalendar.timeInMillis
        datePickerDialog.show()

    }

    private fun isValidInput(): Boolean {
        var result = true
        if (name.isNullOrEmpty()) {
            edtFullName.error = getString(R.string.error_empty_name)
            result = false
        } else if (name!!.length > 100) {
            edtFullName.error = getString(R.string.error_name_too_long)
            result = false
        }

        AccountManager.account?.let { account ->
            if (!account.email.isNullOrEmpty()) {
                if (email.isNullOrEmpty()) {
                    edtEmail.error = getString(R.string.error_empty_email)
                    result = false
                } else if (email!!.length > 100) {
                    edtEmail.error = getString(R.string.error_email_too_long)
                    result = false
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.error = getString(R.string.error_email_invalid)
                    result = false
                }
            }

            if (!account.dob.isNullOrEmpty()) {
                if (birthday.isNullOrEmpty()) {
                    edtBirthday.error = getString(R.string.error_empty_birthday)
                    result = false
                }
            }
        }

        return result
    }

    private fun showAvatar(urlAvatar: String?) {
        Glide
            .with(this@ProfileActivity)
            .load(urlAvatar)
            .circleCrop()
            .into(ivAvatar)
    }

    private val myDateListener =
        OnDateSetListener { _, year, month, date ->
            showDate(year, month, date)
        }

    private fun showDate(year: Int, month: Int, day: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        dateOfBirth = calendar.time

        val sdf = SimpleDateFormat(Constants.dateFormat)
        edtBirthday.setText(sdf.format(calendar.time))
    }

    private fun onSelectChooseImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestAccessImagePermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                return
            }
        }

        chooseImage()
    }

    private fun onSelectCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                requestCameraPermissionLauncher.launch(permission)
                return
            }

        }
        openCamera()
    }

    private fun showChooseImageDialog() {
        /**
         * a selector dialog to display two image source options, from camera
         * ‘Take from camera’ and from existing files ‘Select from gallery’
         */
        val items = arrayOf(
            getString(R.string.take_from_camera),
            getString(R.string.select_from_gallery)
        )
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.select_dialog_item, items
        )
        val builder = AlertDialog.Builder(this)
        builder.setAdapter(adapter) { _, item ->
            // pick from
            // camera
            if (item == 0) {
                onSelectCamera()
            } else {
                onSelectChooseImage()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}