package com.example.dice_application

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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
        val game_button : Button = findViewById(R.id.new_game)

        //popup window for the about
        about_button.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.activity_about)
            dialog.show()
        }


        game_button.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.activity_user_input)

            var nextbutton = dialog.findViewById(R.id.next_button_input) as Button
            var target_id = dialog.findViewById(R.id.target_score_input) as EditText
            var name_id = dialog.findViewById(R.id.PersonName) as EditText

            nextbutton.setOnClickListener {
                val intent = Intent(this, game::class.java)
                intent.putExtra("key", target_id.text.toString())
                intent.putExtra( "key2", name_id.text.toString() )
                startActivity(intent)
                dialog.dismiss()
            }
            dialog.show()
        }

    }
}