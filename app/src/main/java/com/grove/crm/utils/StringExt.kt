package com.grove.crm.utils

import android.text.Html

fun String.htmlToString() : String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this ).toString()
    }
}

