package com.example.k_zaiem.myapplicationdb

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import java.io.IOException

class Insertion : AppCompatActivity() {
    lateinit var mDatabaseRef: DatabaseReference

    lateinit var  mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)
        mAuth = FirebaseAuth.getInstance()

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(profil.FB_DATABASE_PATH)
        val registerBtn = findViewById<View>(R.id.regBtn) as Button

        registerBtn.setOnClickListener(View.OnClickListener {
            view -> registerUser()
        })


    }
    private fun registerUser () {
        val emailTxt = findViewById<View>(R.id.EditEmail) as TextInputEditText
        val passwordTxt = findViewById<View>(R.id.mdpTxt) as TextInputEditText

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()


        if (!email.isEmpty() && !password.isEmpty()) {
            this.mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val uid = user!!.uid
                    mDatabaseRef!!.child(uid).child("Email").setValue(email)
                    mDatabaseRef!!.child(uid).child("Mot de pass").setValue(password)
                    Toast.makeText(this, R.string.succès, Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, profil::class.java))
                } else {
                    Toast.makeText(this, R.string.erreur, Toast.LENGTH_LONG).show()


                }
            }
        }else {
            Toast.makeText(this,R.string.remplir_les_champ, Toast.LENGTH_LONG).show()
        }




    }




}


