package com.quynhlamryan.crm.ui.otp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.quynhlamryan.crm.Constants
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.ApiClient.lazyMgr
import com.quynhlamryan.crm.ui.main.MainActivity
import com.quynhlamryan.crm.ui.policy.PolicyActivity
import com.quynhlamryan.crm.utils.AccountManager
import com.quynhlamryan.crm.utils.CustomProgressDialog
import com.quynhlamryan.crm.utils.Logger
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var otpViewModel: OtpViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationInProgress = false
    private var storedVerificationId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        otpViewModel = ViewModelProvider(this)
            .get(OtpViewModel::class.java)

        btnNext.setOnClickListener {
            val code = edtOtp.text.toString()
            if (TextUtils.isEmpty(code)) {
                edtOtp.error = "Cannot be empty."
                return@setOnClickListener
            }
//            if (storedVerificationId.isNullOrEmpty()) {
//                val phoneWithCode = "${Constants.phoneCountryCode}${AccountManager.phone}"
//                startPhoneNumberVerification(phoneWithCode)
//                return@setOnClickListener
//            }
            verifyPhoneNumberWithCode(storedVerificationId, code)

        }

        auth = Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                CustomProgressDialog.dismissProgressDialog()
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Logger.d("onVerificationCompleted:$credential")
                // [START_EXCLUDE silent]
                verificationInProgress = false
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, cred = credential)
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                CustomProgressDialog.dismissProgressDialog()
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Logger.w("onVerificationFailed", e)
                // [START_EXCLUDE silent]
                verificationInProgress = false
                // [END_EXCLUDE]

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
//                    binding.fieldPhoneNumber.error = "Invalid phone number."
                    // [END_EXCLUDE]
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                        Snackbar.LENGTH_SHORT).show()
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED)
                // [END_EXCLUDE]
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                CustomProgressDialog.dismissProgressDialog()
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Logger.d("onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT)
                // [END_EXCLUDE]
            }
        }
        // [END phone_auth_callbacks]
    }

    override fun onStart() {
        super.onStart()
        val phoneWithCode = "${Constants.phoneCountryCode}${AccountManager.phone}"
        startPhoneNumberVerification(phoneWithCode)
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
    private fun openPolicyActivity() {
        val intent = Intent(this, PolicyActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        CustomProgressDialog.showProgressDialog(this)
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]

        verificationInProgress = true
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        verificationId?.let {verificationId ->
            try {
                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential)
            } catch (e: IllegalArgumentException) {
                CustomProgressDialog.dismissProgressDialog()
                Logger.e(e)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        CustomProgressDialog.showProgressDialog(this)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                CustomProgressDialog.dismissProgressDialog()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Logger.d("signInWithCredential:success")

                    val user = task.result?.user
                    // [START_EXCLUDE]
                    updateUI(STATE_SIGNIN_SUCCESS, user)

                    user?.uid?.let { userUID ->
                        CustomProgressDialog.showProgressDialog(this)
                        otpViewModel.verifyOtp(userUID)?.observe(this, {
                            CustomProgressDialog.dismissProgressDialog()
                            val token = it ?: return@observe
                            AccountManager.token = token
                            lazyMgr.reset()
                            otpViewModel.getAccount()?.observe(this, Observer { it ->
                                val account = it ?: return@Observer
                                AccountManager.account = account
                                if (!account.isNew) {
                                    openMainActivity()
                                } else {
                                    openPolicyActivity()
                                }
                            })
                        })
                    }

                    // [END_EXCLUDE]
                } else {
                    // Sign in failed, display a message and update the UI
                    Logger.w("signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        // [START_EXCLUDE silent]
//                        binding.fieldVerificationCode.error = "Invalid code."
                        // [END_EXCLUDE]
                    }
                    // [START_EXCLUDE silent]
                    // Update UI
                    updateUI(STATE_SIGNIN_FAILED)
                    // [END_EXCLUDE]
                }
            }
    }

    private fun updateUI(
        uiState: Int,
        user: FirebaseUser? = auth.currentUser,
        cred: PhoneAuthCredential? = null
    ) {
        when (uiState) {
            STATE_INITIALIZED -> {
                // Initialized state, show only the phone number field and start button
//                enableViews(binding.buttonStartVerification, binding.fieldPhoneNumber)
//                disableViews(binding.buttonVerifyPhone, binding.buttonResend, binding.fieldVerificationCode)
//                binding.detail.text = null
            }
            STATE_CODE_SENT -> {
                // Code sent state, show the verification field, the
//                enableViews(binding.buttonVerifyPhone, binding.buttonResend,
//                    binding.fieldPhoneNumber, binding.fieldVerificationCode)
//                disableViews(binding.buttonStartVerification)
//                binding.detail.setText(R.string.status_code_sent)
            }
            STATE_VERIFY_FAILED -> {
                // Verification has failed, show all options
//                enableViews(binding.buttonStartVerification, binding.buttonVerifyPhone,
//                    binding.buttonResend, binding.fieldPhoneNumber,
//                    binding.fieldVerificationCode)
//                binding.detail.setText(R.string.status_verification_failed)
            }
            STATE_VERIFY_SUCCESS -> {
                // Verification has succeeded, proceed to firebase sign in
//                disableViews(binding.buttonStartVerification, binding.buttonVerifyPhone,
//                    binding.buttonResend, binding.fieldPhoneNumber,
//                    binding.fieldVerificationCode)
//                binding.detail.setText(R.string.status_verification_succeeded)

                // Set the verification text based on the credential
                if (cred != null) {
//                    if (cred.smsCode != null) {
//                        binding.fieldVerificationCode.setText(cred.smsCode)
//                    } else {
//                        binding.fieldVerificationCode.setText(R.string.instant_validation)
//                    }
                }
            }
            STATE_SIGNIN_FAILED -> {}
                // No-op, handled by sign-in check
//                binding.detail.setText(R.string.status_sign_in_failed)
            STATE_SIGNIN_SUCCESS -> {
            }
        } // Np-op, handled by sign-in check

        if (user == null) {
            // Signed out
//            binding.phoneAuthFields.visibility = View.VISIBLE
//            binding.signedInButtons.visibility = View.GONE

//            binding.status.setText(R.string.signed_out)
        } else {
            // Signed in
//            binding.phoneAuthFields.visibility = View.GONE
//            binding.signedInButtons.visibility = View.VISIBLE
//
//            enableViews(binding.fieldPhoneNumber, binding.fieldVerificationCode)
//            binding.fieldPhoneNumber.text = null
//            binding.fieldVerificationCode.text = null
//
//            binding.status.setText(R.string.signed_in)
//            binding.detail.text = getString(R.string.firebase_status_fmt, user.uid)
        }
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
        private const val KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress"
        private const val STATE_INITIALIZED = 1
        private const val STATE_VERIFY_FAILED = 3
        private const val STATE_VERIFY_SUCCESS = 4
        private const val STATE_CODE_SENT = 2
        private const val STATE_SIGNIN_FAILED = 5
        private const val STATE_SIGNIN_SUCCESS = 6
    }

}