package com.example.k_zaiem.myapplicationdb

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_insertion.*
import kotlinx.android.synthetic.main.activity_profil.*

import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class profil : AppCompatActivity() {

    lateinit var Enregistrer: Button


    lateinit var ProfilName: android.support.design.widget.TextInputEditText
    lateinit var Tel: android.support.design.widget.TextInputEditText
    lateinit var Date: TextView

    lateinit var SelectImage:  de.hdodenhof.circleimageview.CircleImageView


    internal var FilePathUri: Uri? = null

    // Creating StorageReference and DatabaseReference object.
    lateinit var storageReference: StorageReference
    lateinit var databaseReference: DatabaseReference
    lateinit  var mAuth: FirebaseAuth


    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        databaseReference = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH)
        Enregistrer = findViewById<View>(R.id.Enregistrer) as Button

        Date = findViewById<View>(R.id.DateEditText) as TextView
        Tel = findViewById<View>(R.id.TelEditText) as android.support.design.widget.TextInputEditText
        ProfilName = findViewById<View>(R.id.NameEditText) as android.support.design.widget.TextInputEditText
        SelectImage = findViewById<View>(R.id.ShowImageView) as de.hdodenhof.circleimageview.CircleImageView
        progressDialog = ProgressDialog(this@profil)

        SelectImage.setOnClickListener {

            val options = arrayOf<CharSequence>("choisir via Gallery", "Annuler")
            val builder = AlertDialog.Builder(this@profil)
            builder.setTitle("Ajouter  Photo!")
            builder.setItems(options) { dialog, item ->
                if (options[item] == "choisir via Gallery") {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, "Select image"), profil.REQUEST_CODE)
                } else if (options[item] == "Annuler") {
                    dialog.dismiss()
                }
            }
            builder.show()
        }


        Enregistrer.setOnClickListener {
            EnregistrerProfil()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            FilePathUri = data.data

            try {

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, FilePathUri)


                SelectImage.setImageBitmap(bitmap)


            } catch (e: IOException) {

                e.printStackTrace()
            }

        }
    }

    fun GetFileExtension(uri: Uri): String {

        val contentResolver = contentResolver

        val mimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))

    }


    fun EnregistrerProfil() {


        if (FilePathUri != null) {



            progressDialog.setTitle("Enregistrement...")

            progressDialog.show()

            val storageReference2nd = storageReference.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri!!))
            storageReference2nd.putFile(FilePathUri!!)
                    .addOnSuccessListener { taskSnapshot ->

                        val TempProfilName = ProfilName.text.toString()
                        val TempProfilTel = Tel.text.toString()
                        val TempProfilDate = Date.text.toString()
                        progressDialog.dismiss()

                        Toast.makeText(applicationContext, "Profil Enregistré ", Toast.LENGTH_LONG).show()

                        val imageUploadInfo = ProfileUploadInfo(TempProfilName,TempProfilTel,TempProfilDate,taskSnapshot.downloadUrl!!.toString())

                        val user = mAuth.currentUser
                        val uid = user!!.uid
                        databaseReference.child(uid).child("imageURL").setValue(imageUploadInfo.imageURL)
                        databaseReference.child(uid).child("profilName").setValue(imageUploadInfo.profilName)
                        databaseReference.child(uid).child("profilTel").setValue(imageUploadInfo.profilTel)
                        databaseReference.child(uid).child("Date").setValue(imageUploadInfo.Date)
                    }

                    .addOnFailureListener { exception ->

                        progressDialog.dismiss()


                        Toast.makeText(this@profil, exception.message, Toast.LENGTH_LONG).show()
                    }


                    .addOnProgressListener {

                        progressDialog.setTitle("Enregistrement ...")
                    }

            startActivity(Intent(this, Aceuil::class.java))
        } else {

            Toast.makeText(this@profil, "remplissez les champs s'il vous plait :|", Toast.LENGTH_LONG).show()

        }
    }




    @SuppressLint("SetTextI18n")
 fun funDate (view:View){
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val dpd = DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, DatePickerDialog.OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            Date.text=   "$dayOfMonth/$monthOfYear/$year"
        }, year, month, day)

        //show datepicker
        dpd.show()
    }


    companion object {


        val FB_STORAGE_PATH = "Names/"
        val FB_DATABASE_PATH = "Names"
        val REQUEST_CODE = 12
    }
}