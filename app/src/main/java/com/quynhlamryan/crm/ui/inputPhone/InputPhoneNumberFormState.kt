package com.quynhlamryan.crm.ui.inputPhone

/**
 * Data validation state of the login form.
 */
data class InputPhoneNumberFormState(
    val phoneError: Int? = null,
    val isDataValid: Boolean = false
)