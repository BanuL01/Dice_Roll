package com.example.dice_application

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible

class options : AppCompatActivity() {
    var win_count = 0
    var lose_count = 0
    var hardlevel = false
    lateinit var about_button : Button;
    lateinit var  game_button : Button;
    var isNewgamePopup=false
    var isAboutPopup = false
    var aboutdialogbox: Dialog? = null
    var gamedialogbox: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        aboutdialogbox = Dialog(this)
        gamedialogbox = Dialog(this)

        about_button = findViewById(R.id.about)
        game_button = findViewById(R.id.new_game)

        win_count = intent.getIntExtra("win_count",0)
        lose_count = intent.getIntExtra("lose_count", 0)


        //popup window for the about
        about_button.setOnClickListener {
            isAboutPopup = true
            aboutdialogbox!!.setContentView(R.layout.activity_about)
            aboutdialogbox!!.setOnCancelListener{
                isAboutPopup=false
            }
            aboutdialogbox!!.show()
        }


        game_button.setOnClickListener {
            isNewgamePopup=true
            gamedialogbox!!.setContentView(R.layout.activity_user_input)

            var nextbutton = gamedialogbox!!.findViewById(R.id.next_button_input) as Button
            var target_id = gamedialogbox!!.findViewById(R.id.target_score_input) as EditText
            var name_id = gamedialogbox!!.findViewById(R.id.PersonName) as EditText
            val required_1 = gamedialogbox!!.findViewById(R.id.required1) as TextView
            val requiered_2 = gamedialogbox!!.findViewById(R.id.required2) as TextView
            val hard_switch = gamedialogbox!!.findViewById(R.id.hardSwitch) as Switch


            hard_switch?.setOnCheckedChangeListener { _, isChecked ->
                hardlevel= isChecked
            }

            nextbutton.setOnClickListener {

                println(hardlevel)

                if (name_id.length()== 0 && target_id.length()== 0 ){
                    required_1.isVisible = true
                    requiered_2.isVisible = true
                }
                else if (name_id.length() == 0) {
                    required_1.isVisible = true
                    requiered_2.isVisible = false
                }
                else if (target_id.length()== 0){
                    required_1.isVisible = false
                    requiered_2.isVisible = true
                }
                else {
                    val intent = Intent(this, game::class.java)
                    intent.putExtra("key", target_id.text.toString())
                    intent.putExtra( "key2", name_id.text.toString() )
                    intent.putExtra("win_count",win_count)
                    intent.putExtra("lose_count",lose_count)
                    intent.putExtra("hard_switch", hardlevel)
                    startActivity(intent)
                    gamedialogbox!!.dismiss()

                }
            }
            gamedialogbox!!.setOnCancelListener{
                isNewgamePopup=false
            }
            gamedialogbox!!.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("win_count", win_count)
        outState.putInt("lose_count", lose_count)
        outState.putBoolean("isNewgamePopup", isNewgamePopup)
        outState.putBoolean("isAboutPopup", isAboutPopup)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        win_count = savedInstanceState.getInt("win_count")
        lose_count = savedInstanceState.getInt("lose_count")
        isNewgamePopup = savedInstanceState.getBoolean("isNewgamePopup")
        isAboutPopup = savedInstanceState.getBoolean("isAboutPopup")

        whenRotateSet()
    }

    private fun whenRotateSet() {
        if (isAboutPopup){
            aboutdialogbox!!.setContentView(R.layout.activity_about)
            aboutdialogbox!!.setOnCancelListener{
                isAboutPopup=false
            }
            aboutdialogbox!!.show()
        }
        if (isNewgamePopup){
            gamedialogbox!!.setContentView(R.layout.activity_user_input)
            var nextbutton = gamedialogbox!!.findViewById(R.id.next_button_input) as Button
            var target_id = gamedialogbox!!.findViewById(R.id.target_score_input) as EditText
            var name_id = gamedialogbox!!.findViewById(R.id.PersonName) as EditText
            val required_1 = gamedialogbox!!.findViewById(R.id.required1) as TextView
            val requiered_2 = gamedialogbox!!.findViewById(R.id.required2) as TextView
            val hard_switch = gamedialogbox!!.findViewById(R.id.hardSwitch) as Switch


            hard_switch.setOnCheckedChangeListener { _, isChecked ->
                hardlevel = isChecked
            }

            nextbutton.setOnClickListener {
                intent.putExtra("key",target_id.text.toString())
                if (target_id.text.toString().isEmpty()){
                    val errorMessage = "enter value"
                    target_id.setHint(errorMessage)

                }else{
                    val intent = Intent(this, game::class.java)
                    intent.putExtra("key", target_id.text.toString())
                    intent.putExtra( "key2", name_id.text.toString() )
                    intent.putExtra("win_count",win_count)
                    intent.putExtra("lose_count",lose_count)
                    intent.putExtra("hard_switch", hardlevel)
                    startActivity(intent)
                    gamedialogbox!!.dismiss()
                }
            }
            gamedialogbox!!.setOnCancelListener{
                isNewgamePopup=false
            }
            gamedialogbox!!.show()
        }
    }

    override fun onPause() {
        super.onPause()

        if (aboutdialogbox != null && aboutdialogbox!!.isShowing()) {
            aboutdialogbox!!.dismiss()
        }
        if (gamedialogbox != null && gamedialogbox!!.isShowing()) {
            gamedialogbox!!.dismiss()
        }
    }
}