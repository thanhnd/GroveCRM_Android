package com.grove.crm.ui.order

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.toSpannable
import androidx.lifecycle.Observer
import com.grove.crm.R
import com.grove.crm.data.ProductRepository
import com.grove.crm.data.model.DeliveryInformation
import com.grove.crm.data.model.EInvoiceInformation
import com.grove.crm.data.model.Order
import com.grove.crm.utils.*
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        btnFinishOrder.setOnClickListener {
            submitOrder()
        }
        title = getString(R.string.title_delivery)
        tvTotalPrice.text = getString(R.string.total_price,
            ShoppingCartManager.totalPrice.formatCurrency())

        AccountManager.account?.apply {
            edtPhone.setText(phoneNumber)
            edtFullName.setText(fullName)
        }
    }

    private fun submitOrder() {
        val fullName = edtFullName.text.trim()
        if (fullName.isEmpty()) {
            showAlertDialog(
                title = getString(R.string.dialog_alert_error_title),
                message = "Please input delivery's name.".toSpannable()
            )
            return

        } else if (fullName.length > 100) {
            showAlertDialog(
                title = getString(R.string.dialog_alert_error_title),
                message = "Name should not be longer than 100 characters.".toSpannable()
            )
            return
        }

        val address = edtDeliveryAddress.text.trim()
        if (address.isEmpty()) {
            showAlertDialog(
                title = getString(R.string.dialog_alert_error_title),
                message = "Please input delivery's address.".toSpannable()
            )
            return
        } else if (address.length > 200) {
            showAlertDialog(
                title = getString(R.string.dialog_alert_error_title),
                message = "Address should not be longer than 200 chars.".toSpannable()
            )
            return
        }

        val inputPhone = edtPhone.text.trim().filter { !it.isWhitespace() }

        if (inputPhone.isEmpty()) {
            showAlertDialog(
                title = getString(R.string.dialog_alert_error_title),
                message = "Please input contact phone number".toSpannable()
            )
            return
        } else if (inputPhone.length > 20) {
            showAlertDialog(
                title = getString(R.string.dialog_alert_error_title),
                message = "Phone number should not be longer than 20 chars.".toSpannable()
            )
            return
        } else {
            val isValid = Patterns.PHONE.matcher(inputPhone).matches()
            if (!isValid) {
                showAlertDialog(
                    title = getString(R.string.dialog_alert_error_title),
                    message = "Invalid phone number. Please correct it.".toSpannable()
                )
                return
            }
        }

        val note = edtNote.text.trim()
        if (note.isNotEmpty() && note.length > 300) {
            showAlertDialog(
                title = getString(R.string.dialog_alert_error_title),
                message = "Note should not be longer than 300 chars.".toSpannable()
            )
            return
        }

        var companyName = edtCompanyName.text.trim()
        var companyAddress = edtCompanyAddress.text.trim()
        var companyTaxCode = edtTaxCode.text.trim()

        val deliveryInfo = DeliveryInformation (address.toString(), fullName.toString(), inputPhone.toString(), note.toString())
        val invoiceInformation = EInvoiceInformation (companyName.toString(), companyAddress.toString(), companyTaxCode.toString())
        val order = Order (ShoppingCartManager.listItem, deliveryInfo, invoiceInformation)

        CustomProgressDialog.showProgressDialog(this)
        ProductRepository.submitOrder(order).observe(this, Observer { result ->
            CustomProgressDialog.dismissProgressDialog()
            if (result != null && result) {
                showAlertDialog(
                    title = getString(R.string.dialog_alert_finish_order),
                    message = getString(R.string.finish_order_announcement).toSpannable()
                )
            }
        })
    }
}