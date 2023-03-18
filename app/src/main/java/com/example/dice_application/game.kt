package com.example.dice_application

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class game : AppCompatActivity() {
    val random = Random()

    var com_total = 0
    var user_total = 0
    var computer_list = mutableListOf<Int>()
    var user_list = mutableListOf<Int>()
    var dice_select = mutableListOf<Boolean>(false,false,false,false,false)
    var round_count = 1
    var user_dice_List: MutableList<ImageView>? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val throw_button : Button = findViewById(R.id.throw_button)
        val score : Button = findViewById(R.id.score)
        val round_number : TextView = findViewById(R.id.round_num)



        val com_d1 : ImageView = findViewById(R.id.com_d1)
        val com_d2 : ImageView = findViewById(R.id.com_d2)
        val com_d3 : ImageView = findViewById(R.id.com_d3)
        val com_d4 : ImageView = findViewById(R.id.com_d4)
        val com_d5 : ImageView = findViewById(R.id.com_d5)
        val user_d1 : ImageView = findViewById(R.id.user_d1)
        val user_d2 : ImageView = findViewById(R.id.user_d2)
        val user_d3 : ImageView = findViewById(R.id.user_d3)
        val user_d4 : ImageView = findViewById(R.id.user_d4)
        val user_d5 : ImageView = findViewById(R.id.user_d5)
        val com_score_text : TextView = findViewById(R.id.com_score)
        val user_score_text : TextView = findViewById(R.id.user_score)

        val com_dice_list = mutableListOf<ImageView>(com_d1,com_d2,com_d3,com_d4,com_d5) //dice faces
        user_dice_List = mutableListOf<ImageView>(user_d1,user_d2,user_d3,user_d4,user_d5)

        score.isEnabled = false
        round_number.setText(round_count.toString())
        
        throw_button.setOnClickListener {
            computer_list = genarateList()
            score.isEnabled = true

            if(throw_button.text == "Throw") {
                user_list = genarateList()
                throw_button.setText("Rethrow")
                for (i in 0 until 5){
                    user_dice_List!![i].isClickable = true
                }
            }


            else if (throw_button.text == "Rethrow"){
                throw_button.setText("Rethrow Again")

                for (i in 0 until 5){
                    if (dice_select[i] == false){
                        var rannum = Random().nextInt(6) + 1
                        user_list[i] = rannum
                    }
                }
            }
             else {
                score.isEnabled = false
                round_count++
                round_number.setText(round_count.toString()) //setting the round number
                throw_button.setText("Throw")
                updateScore()
                com_score_text.setText(com_total.toString())
                user_score_text.setText(user_total.toString())
                for (i in 0 until 5){
                    user_dice_List!![i].isClickable = false
                }

                for (i in 0 until 5){
                    if (dice_select[i] == false){
                        var rannum = Random().nextInt(6) + 1
                        user_list[i] = rannum
                    }
                }

                //reset part
                dice_select = mutableListOf<Boolean>(false,false,false,false,false)
                for (index in 0 until 5){
                    dice_select[index] = false
                    user_dice_List?.get(index)?.setBackgroundColor(Color.TRANSPARENT)
                }
            }

            println(computer_list)
            println(user_list)

            for (diceIndex in 0 until 5){
                setImage(com_dice_list[diceIndex],computer_list[diceIndex])
                setImage(user_dice_List!![diceIndex],user_list[diceIndex])
            }

        }

        score.setOnClickListener {
            updateScore()
            round_count++
            round_number.setText(round_count.toString()) //setting the round number
            score.isEnabled = false
            throw_button.setText("Throw")
            dice_select = mutableListOf<Boolean>(false,false,false,false,false)
            for (index in 0 until 5){
                dice_select[index] = false
                user_dice_List?.get(index)?.setBackgroundColor(Color.TRANSPARENT)
            }
            com_score_text.setText(com_total.toString())
            user_score_text.setText(user_total.toString())


//            if(com_total.toString().length == 1){
//                com_score_text.setText("00" + com_total.toString())
//            }
//            else if (com_total.toString().length == 2)
//                com_score_text.setText("0" + com_total.toString())
//
//            else com_score_text.setText(com_total.toString())
//
//            if(user_total.toString().length == 1){
//                user_score_text.setText("00" + user_total.toString())
//            }
//            else if (user_total.toString().length == 2)
//                user_score_text.setText("0" + user_total.toString())
//
//            else user_score_text.setText(user_total.toString())
//
            println(com_total)
            println(user_total)


        }
        for (i in 0 until 5){
            user_dice_List!![i].setOnClickListener {
                selectImage(i)
            }
           user_dice_List!![i].isClickable = false
        }
    }

    private fun selectImage(index: Int) {
        if (dice_select[index] == false){
            dice_select[index] = true
            user_dice_List?.get(index)?.setBackgroundColor(getColor(R.color.brown_background))
        }
        else {
            dice_select[index] = false
            user_dice_List?.get(index)?.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun updateScore() {
        for (itemIndex in 0 .. 4) {
            com_total += computer_list[itemIndex]
            user_total += user_list[itemIndex]
        }
    }

    private fun setImage(image: ImageView, randomValue: Int) {
        if (randomValue==1){
            image.setImageResource(R.drawable.dice_1)
        }else if (randomValue==2){
            image.setImageResource(R.drawable.dice_2)
        }else if (randomValue==3){
            image.setImageResource(R.drawable.dice_3)
        }else if (randomValue==4){
            image.setImageResource(R.drawable.dice_4)
        }else if (randomValue==5){
            image.setImageResource(R.drawable.dice_5)
        }else if (randomValue==6){
            image.setImageResource(R.drawable.dice_6)
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