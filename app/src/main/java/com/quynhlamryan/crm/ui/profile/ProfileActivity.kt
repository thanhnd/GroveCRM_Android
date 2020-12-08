package com.quynhlamryan.crm.ui.profile

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.Constants
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.AccountManager
import com.quynhlamryan.crm.utils.Logger
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.FileNotFoundException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private var dateOfBirth: Date? = null
    lateinit var profileViewModel: ProfileViewModel
    private var name: String? = null
    private var birthday: String? = null
    private var email: String? = null

    var requestImageLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request code
            val data = result.data
            data?.data?.let { selectedImage ->
                val filePath = getPath(selectedImage)
                val file_extn = filePath.substring(filePath.lastIndexOf(".") + 1)
                Logger.d(filePath)
                try {
                    if (file_extn == "img" || file_extn == "jpg" || file_extn == "jpeg" || file_extn == "gif" || file_extn == "png") {
                        profileViewModel.uploadAvatar(filePath)
                            ?.observe(this, Observer { isSuccess ->
                                if (isSuccess) {
                                    finish()
                                }
                            })
                    } else {
                        //NOT IN REQUIRED FORMAT
                    }
                } catch (e: FileNotFoundException) {
                    // TODO Auto-generated catch block
                    Logger.e(e)
                }
            }
        }
    }

    private val requestPermissionLauncher =
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setTitle(R.string.profile_info)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        btnDone.setOnClickListener {
            if (!isValidInput()) {
                return@setOnClickListener
            }
            profileViewModel.updateAccount(name, birthday, email)
                ?.observe(this, Observer { isSuccess ->
                    if (isSuccess) {
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
            if (Build.VERSION.SDK_INT >= 23) {
                val permissionCheck = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    return@setOnClickListener
                }
            }

            chooseImage()
        }
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        dateOfBirth.let {
            calendar.time = dateOfBirth
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
        AccountManager.account?.let { account ->
            if (!account.fullName.isNullOrEmpty()) {
                if (name.isNullOrEmpty()) {
                    edtFullName.error = getString(R.string.error_empty_name)
                    result = false
                } else if (name!!.length > 100) {
                    edtFullName.error = getString(R.string.error_name_too_long)
                    result = false
                }
            }

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

    override fun onStart() {
        super.onStart()

        AccountManager.account?.apply {
            Glide
                .with(this@ProfileActivity)
                .load(urlAvatar)
                .circleCrop()
                .into(ivAvatar)
            edtFullName.setText(fullName)
            edtBirthday.setText(dob)
            edtPhone.setText(phoneNumber)
            edtEmail.setText(email)

            if (!birthday.isNullOrEmpty())  {
                val format = SimpleDateFormat(Constants.dateFormat)
                try {
                    dateOfBirth = format.parse(birthday!!)
                } catch (e: ParseException) {
                    Logger.e(e)
                }
            }
        }
    }

    private val myDateListener =
        OnDateSetListener { view, year, month, date -> // TODO Auto-generated method stub
            // year = year
            // month = month
            // date = day
            showDate(year, month, date)
        }

    private fun showDate(year: Int, month: Int, day: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        dateOfBirth = calendar.time

        val sdf = SimpleDateFormat(Constants.dateFormat)
        edtBirthday.setText(sdf.format(calendar.time))
    }

    companion object {
        const val REQUEST_PERMISSION_EXTERNAL_STORAGE = 1
    }

}