package com.example.k_zaiem.myapplicationdb

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : AppCompatActivity() {
lateinit var imageprofile:ImageView
    lateinit var descri:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        setSupportActionBar(toolbar)
              imageprofile=findViewById<View>(R.id.img_car_expanded) as ImageView


              descri=findViewById<View>(R.id.description) as TextView
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitleAndImage()
    }

    private fun setTitleAndImage() {
        val intent = intent
        val Title = intent.getStringExtra("TITLE")
        supportActionBar!!.title = Title

        val description = intent.getStringExtra("Description")
        descri.text = description




        val Image = intent.getStringExtra("IMAGE")
        Glide.with(applicationContext).load(Image).into(imageprofile)
    }
}
