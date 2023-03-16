package com.example.dice_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.*

class game : AppCompatActivity() {
    val random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        var computer_list = mutableListOf<Int>()
        var user_list = mutableListOf<Int>()

        val throw_button : Button = findViewById(R.id.throw_button)

        throw_button.setOnClickListener {

            computer_list=genarateList()
            user_list=genarateList()

            println(computer_list)
            println(user_list)
        }
    }

    private fun genarateList(): MutableList<Int> {
        var temp_list = mutableListOf<Int>()

        for (i in 0 until 5) {
            var randomNumber = Random().nextInt(6) + 1
            temp_list.add(randomNumber)
        }
        return  temp_list
    }
}