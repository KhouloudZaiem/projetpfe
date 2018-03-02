package com.example.k_zaiem.myapplicationdb

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_gereprofil.*
import kotlinx.android.synthetic.main.activity_profil.*

class Connexion : AppCompatActivity() {
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorageRef: StorageReference
    internal lateinit var mGoogleSignInClient: GoogleSignInClient
    internal lateinit var mp:TextView
    internal lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        mStorageRef = FirebaseStorage.getInstance().reference
        val loginBtn = findViewById<View>(R.id.loginBtn) as Button
        val registerTxt = findViewById<View>(R.id.regTxt) as TextView
         mp=findViewById<View>(R.id.mp)as TextView

        mp.setOnClickListener(View.OnClickListener { view ->
            oublier()
        })


        loginBtn.setOnClickListener(View.OnClickListener { view ->
            login()
        })

        registerTxt.setOnClickListener(View.OnClickListener { view ->
            register()
        })

        mAuth = FirebaseAuth.getInstance()

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<View>(R.id.sign_in).setOnClickListener { signIn() }


    }

    private fun oublier() {
        startActivity(Intent(this, MotDePasse::class.java))
        }


    private fun login() {

        val emailTxt = findViewById<View>(R.id.emailTxt) as TextInputEditText
        var email = emailTxt.text.toString()
        val passwordTxt = findViewById<View>(R.id.passwordTxt) as TextInputEditText
        var password = passwordTxt.text.toString()


        if (!email.isEmpty() && !password.isEmpty()) {
            this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, Aceuil::class.java))
                    Toast.makeText(this, R.string.succès, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, R.string.erreur, Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(this, R.string.remplir_les_champ, Toast.LENGTH_SHORT).show()
        }
    }

    private fun register() {
        startActivity(Intent(this, Insertion::class.java))
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Sign In was successful, authenticate with Firebase
                val account = task.getResult<ApiException>(ApiException::class.java)

                //authenticating with firebase
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this@Connexion, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        //getting the auth credential
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {


                        val user = mAuth.currentUser

                        Toast.makeText(this@Connexion, R.string.succès, Toast.LENGTH_SHORT).show()
                        mDatabase = FirebaseDatabase.getInstance().getReference("Names")
                        val uid = user!!.uid
                        val nom = user!!.displayName
                        val email = user!!.email
                        val imgurl = user!!.photoUrl.toString()
                        val numero = user!!.phoneNumber.toString()
                        mDatabase!!.child(uid).child("profilName").setValue(nom)
                        mDatabase!!.child(uid).child("profilTel").setValue(numero)
                        mDatabase!!.child(uid).child("Email").setValue(email)
                        mDatabase!!.child(uid).child("imageURL").setValue(imgurl)

                        startActivity(Intent(this, Aceuil::class.java))
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(this@Connexion, R.string.erreur,
                                Toast.LENGTH_SHORT).show()

                    }

                }
    }


    //this method is called on click
    fun signIn() {
        //getting the google signin intent
        val signInIntent = mGoogleSignInClient.signInIntent

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        private val RC_SIGN_IN = 234
    }


}