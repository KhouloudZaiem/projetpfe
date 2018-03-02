package com.example.k_zaiem.myapplicationdb

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.MimeTypeMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import java.io.IOException
import android.app.DatePickerDialog
import android.icu.text.Collator
import android.text.Editable
import android.text.style.TtsSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.*
import java.util.*


class gereprofil : AppCompatActivity() {




    lateinit var mStorageRef: StorageReference
    lateinit var imgUri: Uri
    lateinit var imageprofil: ImageView
    lateinit var mDatabase: DatabaseReference
    var user = FirebaseAuth.getInstance().currentUser
    lateinit var nom:EditText
    lateinit var datep:EditText
    lateinit var numero:EditText
    lateinit var pass:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gereprofil)
        mStorageRef = FirebaseStorage.getInstance().reference
        val modif = findViewById<View>(R.id.modifier) as Button
        modif.setOnClickListener { modifier() }
        val desaciver = findViewById<View>(R.id.desativer) as Button
        desaciver.setOnClickListener { desactiver() }

        val modifphoto = findViewById<View>(R.id.textViewmodif) as TextView
        modifphoto.setOnClickListener { modiferimg() }
        imageprofil = findViewById<View>(R.id.imageprofil) as de.hdodenhof.circleimageview.CircleImageView
        imageprofil.setOnClickListener { selectimage() }



        nom = findViewById<View>(R.id.profilnom) as EditText
        datep = findViewById<View>(R.id.profildate) as EditText
        numero = findViewById<View>(R.id.profiltel) as EditText
        pass = findViewById<View>(R.id.profilpass) as EditText





        var uid = user!!.uid

        mDatabase = FirebaseDatabase.getInstance().getReference("Names")

        mDatabase.child(uid).child("profilName").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                nom.hint = snapshot.value.toString()
            }


        })
        mDatabase.child(uid).child("Date").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                datep.hint = snapshot.value.toString()
            }


        })
        mDatabase.child(uid).child("profilTel").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                numero.hint = snapshot.value.toString()
            }


        })




        mDatabase.child(uid).child("imageURL").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                var  url=snapshot.value.toString()
                Glide.with(applicationContext).load(url).into(imageprofil)

            }
        })


    }

   fun desactiver() {
       val options = arrayOf<CharSequence>("Oui", "Non")
       val builder = AlertDialog.Builder(this@gereprofil)
       builder.setTitle("Vous étes sur!")
       builder.setItems(options) { dialog, item ->
           if (options[item] == "Oui") {
               user!!.delete()
               startActivity(Intent(this, Connexion::class.java))
               Toast.makeText(this@gereprofil, R.string.desactiver, Toast.LENGTH_SHORT).show()

           } else if (options[item] == "Non") {
               dialog.dismiss()
           }
       }
       builder.show()

   }


    fun selectimage() {
        val options = arrayOf<CharSequence>("choisir via Gallery", "Annuler")
        val builder = AlertDialog.Builder(this@gereprofil)
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






    private fun modiferimg() {



        if (imgUri != null) {




            val storageReference2nd = mStorageRef.child(profil.FB_STORAGE_PATH + System.currentTimeMillis() + "." + GetFileExtension(imgUri!!))
            storageReference2nd.putFile(imgUri!!)
                    .addOnSuccessListener { taskSnapshot ->



                        val imageUploadInfo = taskSnapshot.downloadUrl!!.toString()


                        val uid = user!!.uid
                        mDatabase.child(uid).child("imageURL").setValue(imageUploadInfo)

                    }

                    .addOnFailureListener { exception ->


                        Toast.makeText(this@gereprofil, exception.message, Toast.LENGTH_LONG).show()
                    }


                    .addOnProgressListener {

                    }
            Toast.makeText(this@gereprofil, R.string.profil_Modifier, Toast.LENGTH_LONG).show()
            startActivity(Intent(this, AfficheP::class.java))
        }


        else {


        }



    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == profil.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imgUri = data.data

            try {
                val bm = MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
                imageprofil!!.setImageBitmap(bm)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
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




     fun modifier() {
         var uid = user!!.uid
         val datetxt = datep.text.toString()
         val nomtxt = nom.text.toString()
         val passtxt = pass.text.toString()
         val numerotxt = numero.text.toString()

         if (!nomtxt.isEmpty()) {
             mDatabase.child(uid).child("profilName").setValue(nomtxt)
         }


         if (!datetxt.isEmpty()) {
             mDatabase.child(uid).child("Date").setValue(datetxt)
         }



         if (!numerotxt.isEmpty()) {
             mDatabase.child(uid).child("profilTel").setValue(numerotxt);
         }


         if (!passtxt.isEmpty()) {
             val Confirmer = findViewById<View>(R.id.confirm) as EditText
             val passcongi = Confirmer.text.toString()


             mDatabase.child(uid).child("Mot de pass").addValueEventListener(object : ValueEventListener {
                 override fun onCancelled(p0: DatabaseError?) {}
                 override fun onDataChange(snapshot: DataSnapshot) {
                     val X: String
                     X = snapshot.value.toString()

                     if (X == passcongi) {
                         mDatabase.child(uid).child("Mot de pass").setValue(passtxt)
                         user!!.updatePassword(passtxt)
                         Toast.makeText(this@gereprofil, R.string.mdp_Modifier, Toast.LENGTH_SHORT).show()

                     }
                     else{

                         Toast.makeText(this@gereprofil, R.string.mdp_Incorrect, Toast.LENGTH_SHORT).show()


                     }
                 }


             })




         }


         startActivity(Intent(this, AfficheP::class.java))

     }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.retour, menu)

        return true
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.retourner) {
            startActivity(Intent(this, AfficheP::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun <TResult> Task<TResult>.addOnCompleteListener(gereprofil: gereprofil, onCompleteListener: OnCompleteListener<AuthResult>) {}
}