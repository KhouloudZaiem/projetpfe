package com.example.k_zaiem.myapplicationdb

import android.content.Intent
import android.graphics.PixelFormat
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout

class Splach : AppCompatActivity() {
    internal lateinit var splashTread: Thread


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val Window = window
        Window.setFormat(PixelFormat.RGBX_8888)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach)
        StartAnimation()

    }

    fun StartAnimation() {


        var anim = AnimationUtils.loadAnimation(this, R.anim.alpha)
        anim.reset()
        val l = findViewById<View>(R.id.lin_lay) as LinearLayout
        l.clearAnimation()
        l.startAnimation(anim)


        anim = AnimationUtils.loadAnimation(this, R.anim.rotate)
        anim.reset()
        val iv = findViewById<View>(R.id.imageView) as ImageView
        iv.clearAnimation()
        iv.startAnimation(anim)
        splashTread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    // Splash screen pause time
                    while (waited < 3500) {
                        Thread.sleep(100)
                        waited += 100
                    }
                    val intent = Intent(this@Splach,
                            Connexion::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    startActivity(intent)
                    this@Splach.finish()
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {
                    this@Splach.finish()
                }

            }
        }
        splashTread.start()
    }

}
