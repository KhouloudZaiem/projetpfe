package com.example.k_zaiem.myapplicationdb

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

/**
 * Created by K_Zaiem on 22/02/2018.
 */
class NewsAdapter(internal var list: List<News>, internal var context: Context) : RecyclerView.Adapter<NewsAdapter.MyHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoder {

        val view = LayoutInflater.from(context).inflate(R.layout.card_view_layout, parent, false)


        return MyHoder(view)
    }

    override fun onBindViewHolder(holder: MyHoder, position: Int) {

        val mylist = list[position]

        holder.Date.text=mylist.Date
        holder.titre.text = mylist.Titre
        holder.description.text = mylist.Description
       Glide.with(context).load(mylist.imageURL).into(holder.imageView);
        holder.imageView.setOnClickListener { startNewsDetailActivity(mylist) }

    }

    private fun startNewsDetailActivity(mylist: News) {

        val i = Intent(context, NewsDetailActivity::class.java)
        i.putExtra("IMAGE", mylist.imageURL)
        i.putExtra("TITLE", mylist.Titre)
        i.putExtra("Description", mylist.Description)
        i.putExtra("Date", mylist.Date)
        context.startActivity(i)


    }


    override fun getItemCount(): Int {

        var arr = 0

        try {
            if (list.size == 0) {

                arr = 0

            } else {

                arr = list.size
            }


        } catch (e: Exception) {


        }

        return arr

    }

    inner class MyHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val imageView: ImageView = itemView.findViewById(R.id.img_news)
        val titre: TextView = itemView.findViewById(R.id.text_news_title)
        val description: TextView = itemView.findViewById(R.id.text__news_desc)
val Date:TextView=itemView.findViewById(R.id.text__news_date)

        }


}