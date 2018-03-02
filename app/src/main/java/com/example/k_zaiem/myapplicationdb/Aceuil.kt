package com.example.k_zaiem.myapplicationdb

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_aceuil.*
import kotlinx.android.synthetic.main.app_bar_aceuil.*
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

import kotlin.collections.ArrayList


class Aceuil : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var mDatabase: DatabaseReference
    var user = FirebaseAuth.getInstance().currentUser
    var mAuth = FirebaseAuth.getInstance()
    lateinit var imageprofile:de.hdodenhof.circleimageview.CircleImageView
    lateinit var list: MutableList<News>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aceuil)

        var uid = user!!.uid
        mDatabase = FirebaseDatabase .getInstance().getReference("Nouveaute")

        val  recycle = findViewById<View>(R.id.recycle) as RecyclerView

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list= ArrayList()
                for (dataSnapshot1 in dataSnapshot.children) {

                    val value = dataSnapshot1.getValue(News::class.java)
                    val fire = News()

                   val name = value!!.Titre
                     fire.Titre = name


                    val xx = value!!.imageURL
                    fire.imageURL = xx


                    val email = value!!.Description
                    fire.Description = email

                    val date = value!!.Date
                    fire.Date = date


                    list.add(fire)


                    val recyclerAdapter = NewsAdapter(list, this@Aceuil)
                    val recyce = GridLayoutManager(this@Aceuil, 1)
                    recycle.layoutManager = recyce
                    recycle.itemAnimator = DefaultItemAnimator()
                    recycle.adapter = recyclerAdapter



                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })









        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        val header = nav_view.getHeaderView(0)
        val name= header.findViewById(R.id.usern) as TextView
        val loginuser= header.findViewById(R.id.userlog) as TextView
        val ref= FirebaseDatabase .getInstance().getReference("Names")

        imageprofile=header.findViewById<View>(R.id.imageView) as de.hdodenhof.circleimageview.CircleImageView

        nav_view.setNavigationItemSelectedListener(this)


    loginuser.text = user!!.email

        ref.child(uid).child("profilName").addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError?) {
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            name.text = snapshot.value.toString()

        }
    })




        ref.child(uid).child("imageURL").addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError?) {
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            var url = snapshot.value.toString()
            Glide.with(applicationContext).load(url).into(imageprofile)
        }


    })


}























    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
       }
    }





    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.Accueil -> {
                // Handle the camera action
            }
            R.id.Profil -> {
                startActivity(Intent(this, AfficheP::class.java))
            }
            R.id.deconnecion -> {
                if (mAuth != null) {
                 mAuth.signOut()
                startActivity(Intent(this, Connexion::class.java))

            }}}


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    }
