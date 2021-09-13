package com.grove.crm.ui.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.grove.crm.R
import com.grove.crm.data.model.Language


class LanguageAdapter  // invoke the suitable constructor of the ArrayAdapter class
    (context: Context, arrayList: List<Language>, private val onItemClick: (Language) -> Unit) :
    ArrayAdapter<Language?>(context, 0, arrayList) {
    override fun getView(position: Int,  convertView: View?, parent: ViewGroup): View {

        // convertView which is recyclable view 
        var currentItemView = convertView

        // of the recyclable view is null then inflate the custom layout for the same 
        if (currentItemView == null) {
            currentItemView =
                LayoutInflater.from(context).inflate(R.layout.item_language, parent, false)
        }

        // get the position of the view from the ArrayAdapter 
        getItem(position)?.apply {
            // then according to the position of the view assign the desired TextView 1 for the same
            val tvLanguageName = currentItemView!!.findViewById<TextView>(R.id.tvLanguageName)
            tvLanguageName.text = name
            currentItemView.setOnClickListener {
                onItemClick(this)
            }
        }

        // then return the recyclable view 
        return currentItemView!!
    }
}
