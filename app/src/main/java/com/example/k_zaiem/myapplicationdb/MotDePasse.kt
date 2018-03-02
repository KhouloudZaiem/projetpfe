package com.example.k_zaiem.myapplicationdb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class MotDePasse : AppCompatActivity() {
    private var edtEmail: EditText? = null
    private var btnResetPassword: Button? = null
    private var btnBack: Button? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mot_de_passe)
        edtEmail = findViewById(R.id.edt_reset_email) as EditText
        btnResetPassword = findViewById(R.id.btn_reset_password) as Button
        btnBack = findViewById(R.id.btn_back) as Button

        mAuth = FirebaseAuth.getInstance()

        btnResetPassword!!.setOnClickListener(object : View.OnClickListener {
           override fun onClick(v: View) {

                val email = edtEmail!!.getText().toString().trim()

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(applicationContext, R.string.Entrer_email, Toast.LENGTH_SHORT).show()
                    return
                }

                mAuth!!.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this@MotDePasse, R.string.reset, Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MotDePasse, R.string.erreur, Toast.LENGTH_SHORT).show()
                                }
                            }

            }
        })

        btnBack!!.setOnClickListener(object : View.OnClickListener {
         override   fun onClick(v: View) {
                finish()
            }
        })
    }

}
