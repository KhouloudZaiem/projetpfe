package com.example.k_zaiem.myapplicationdb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.bumptech.glide.Glide


class AfficheP : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    var user = FirebaseAuth.getInstance().currentUser

    lateinit var profilePhoto: ImageView
    lateinit var profilemail: TextView
    lateinit var profileName: TextView
    lateinit var profildate: TextView
    lateinit var profiltel: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affiche_p)
         profilemail = findViewById<View>(R.id.profilemail) as TextView
        profileName = findViewById<View>(R.id.profile_name) as TextView
        profildate = findViewById<View>(R.id.profilpre) as TextView
        profilePhoto = findViewById<View>(R.id.circleView) as ImageView
        profiltel = findViewById<View>(R.id.profiltel) as TextView


        var uid = user!!.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("Names")
        profilemail.text = user!!.email



        mDatabase.child(uid).child("profilName").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                profileName.text = snapshot.value.toString()
            }


        })


        mDatabase.child(uid).child("Date").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                profildate.text = snapshot.value.toString()
            }
        })




        mDatabase.child(uid).child("profilTel").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                profiltel.text = snapshot.value.toString()
            }
        })






        mDatabase.child(uid).child("imageURL").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var url = snapshot.value.toString()
                Glide.with(applicationContext).load(url).into(profilePhoto)

            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.retour, menu)
        menuInflater.inflate(R.menu.gestionprofile, menu)

        return true
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == R.id.action_edit_profile) {
            startActivity(Intent(this, gereprofil::class.java))
            return true
        }
        if (id == R.id.retourner) {
            startActivity(Intent(this, Aceuil::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)

}}
