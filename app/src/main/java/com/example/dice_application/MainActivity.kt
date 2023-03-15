package com.example.dice_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val next_button : Button = findViewById(R.id.Next)
//        next_button.setOnClickListener {
//            val intent = Intent(this, options::class.java)
//            startActivity(intent)
//
//            }
        // Next page connect
        //        //source - https://www.geeksforgeeks.org/how-to-create-a-splash-screen-in-android-using-kotlin/
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, options::class.java)
            startActivity(intent)
            finish()
        },2500)

    }
}