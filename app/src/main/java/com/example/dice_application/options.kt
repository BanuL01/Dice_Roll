package com.example.dice_application

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button

class options : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

//        val new_game_button : Button = findViewById(R.id.new_game)
//        new_game_button.setOnClickListener {
//            val intent = Intent(this, about::class.java)
//            startActivity(intent)
//        }

        val about_button : Button = findViewById(R.id.about)

        about_button.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.activity_about)
            dialog.show()
        }

    }
}