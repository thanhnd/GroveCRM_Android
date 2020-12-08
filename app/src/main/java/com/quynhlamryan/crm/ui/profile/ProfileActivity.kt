package com.quynhlamryan.crm.ui.profile

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.Constants
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.AccountManager
import com.quynhlamryan.crm.utils.Logger
import kotlinx.android.synthetic.main.activity_profile.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : AppCompatActivity() {
    private var dateOfBirth: Date? = null
    lateinit var profileViewModel: ProfileViewModel
    private var name: String? = null
    private var birthday: String? = null
    private var email: String? = null

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

}