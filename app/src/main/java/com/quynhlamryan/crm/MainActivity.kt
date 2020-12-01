package com.quynhlamryan.crm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.ui.main.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityViewModel: MainActivityViewModel
    private val adapter = MainAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        rvMain.adapter = adapter

    }

    override fun onStart() {
        super.onStart()

        mainActivityViewModel.getArticles()!!.observe(this, Observer { articles ->
            adapter.articles = articles
        })
    }
}