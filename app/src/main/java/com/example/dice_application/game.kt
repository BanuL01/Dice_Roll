package com.example.dice_application

import android.app.Dialog
import android.content.Intent
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
    var win_count = 0
    var lose_count = 0
    var user_dice_List: MutableList<ImageView>? =null
    var target_score = 101
    var isTie=false
    var hard_level = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        win_count = intent.getIntExtra("win_count",0)
        lose_count = intent.getIntExtra("lose_count", 0)
        hard_level = intent.getBooleanExtra("hard_switch", false)
        println(hard_level)

        val ts_input : TextView = findViewById(R.id.ts_value)
        val name_id : TextView = findViewById(R.id.user_text)
        target_score = Integer.parseInt(intent.getStringExtra("key"))
        ts_input.text = target_score.toString()
        val user_text = (intent.getStringExtra("key2"))
        name_id.text = user_text.toString()

        val throw_button : Button = findViewById(R.id.throw_button)
        val score : Button = findViewById(R.id.score)
        val round_number : TextView = findViewById(R.id.round_num)
        val H_text : TextView = findViewById(R.id.H)
        val C_text : TextView = findViewById(R.id.C)
        H_text.setText("H : "+ win_count.toString())
        C_text.setText("C : "+ lose_count.toString())

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
            if (!isTie){
                score.isEnabled = true
            }


            if(throw_button.text == "Throw") {
                user_list = genarateList()
                computer_list = genarateList()
                if (!isTie){
                    throw_button.setText("Rethrow")
                    for (i in 0 until 5){
                        user_dice_List!![i].isClickable = true  //
                    }
                }
            }

            else if (throw_button.text == "Rethrow"){
                throw_button.setText("Rethrow Again")

                //updating dices again randomly
                for (i in 0 until 5){
                    if (dice_select[i] == false){
                        var rannum = Random().nextInt(6) + 1
                        user_list[i] = rannum
                    }
                }
            }
             else {
                if (hard_level == true){
                    hardComputerPlayerStrategy()
                }
                else{
                    ComputerRandom()
                    ComputerRandom()
                }

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

                //giving random values to the non selected dices
                for (i in 0 until 5){
                    if (dice_select[i] == false){
                        var rannum = Random().nextInt(6) + 1
                        user_list[i] = rannum
                    }
                }

                winCheck()

                //reset part
                dice_select = mutableListOf<Boolean>(false,false,false,false,false)
                for (index in 0 until 5){
                    dice_select[index] = false
                    user_dice_List?.get(index)?.setBackgroundColor(Color.TRANSPARENT)
                }
            }

            if (isTie){
                round_count++
                round_number.setText(round_count.toString()) //setting the round number

                updateScore()
                com_score_text.setText(com_total.toString())
                user_score_text.setText(user_total.toString())

                winCheck()

            }

            for (diceIndex in 0 until 5){
                setImage(com_dice_list[diceIndex],computer_list[diceIndex])
                setImage(user_dice_List!![diceIndex],user_list[diceIndex])
            }
        }

        score.setOnClickListener {
            if (hard_level == true){
                hardComputerPlayerStrategy()
                println("hard")
            }
            else{
                ComputerRandom()
                ComputerRandom()
                println("ezy")
            }

            //set image
            for (diceIndex in 0 until 5){
                setImage(com_dice_list[diceIndex],computer_list[diceIndex])
            }

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
            for (i in 0 until 5){
                user_dice_List!![i].isClickable = false
            }


            com_score_text.setText(com_total.toString())
            user_score_text.setText(user_total.toString())


            winCheck()

        }
        for (i in 0 until 5){
            user_dice_List!![i].setOnClickListener {
                selectImage(i)
            }
           user_dice_List!![i].isClickable = false
        }
    }

    private fun winCheck() {
//        user_total = 200  // tie check
//        com_total = 200

        if (user_total >= target_score || com_total >= target_score) {
            if (user_total > com_total) {
                win_count ++
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.activity_result)
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setOnCancelListener {
                    val intent = Intent(this, options::class.java)
                    intent.putExtra("win_count",win_count)
                    intent.putExtra("lose_count",lose_count)
                    startActivity(intent)
                }
                dialog.show()
            } else if (user_total < com_total) {
                lose_count ++
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.activity_result)
                val resultText = dialog.findViewById<TextView>(R.id.win_text)
                resultText.text = "YOU LOST"
                resultText.setTextColor(Color.RED)
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(false)
                dialog.setOnCancelListener {
                    val intent = Intent(this, options::class.java)
                    intent.putExtra("win_count",win_count)
                    intent.putExtra("lose_count",lose_count)
                    startActivity(intent)

                }
                dialog.show()

            } else {
                isTie = true
            }
        }
    }

    //function to select dices
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

    //function to update the computer and user totals
    private fun updateScore() {
        for (itemIndex in 0 .. 4) {
            com_total += computer_list[itemIndex]
            user_total += user_list[itemIndex]
        }
    }

    // setting values to the dice images
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

    fun ComputerRandom(){
        val randomBoolean = if (Random().nextInt(2) == 0) false else true
        var com_random = mutableListOf(if (Random().nextInt(2) == 0) false else true,if (Random().nextInt(2) == 0) false else true,if (Random().nextInt(2) == 0) false else true,if (Random().nextInt(2) == 0) false else true,if (Random().nextInt(2) == 0) false else true)

        if (randomBoolean){
            //giving random values to the non selected dices
            for (i in 0 until 5){
                if (com_random[i] == false){
                    var rannum = Random().nextInt(6) + 1
                    computer_list[i] = rannum
                }
            }
        }

    }


    private fun hardComputerPlayerStrategy() {
        var tempcomputerscore=com_total
        var gap = user_total - tempcomputerscore

        //throw
        if (gap > 20){
            //high
            //checking high values
            for (count in  0 ..4){
                var randomindex = random.nextInt(5)
                if (computer_list[randomindex] <= 3){
                    computer_list[randomindex]=random.nextInt(6) + 1
                }
            }
        }else if(gap < 20){
            ComputerRandom()
            ComputerRandom()
        }else {
            //normal
            ComputerRandom()
            for (count in  0 ..4){
                var randomindex = random.nextInt(5)
                if (computer_list[randomindex] <= 2){
                    computer_list[randomindex]=random.nextInt(6) + 1
                }
            }
        }

        for (i in 0..4) {
            tempcomputerscore += computer_list[i]
        }

        //second throw
        if ((tempcomputerscore<(user_total+10))){
            var gap = user_total - tempcomputerscore

            if (gap > 20){
                //high
                //checking high values
                for (count in  0 ..4){
                    var randomindex = random.nextInt(5) + 1
                    if (computer_list[randomindex] <= 3){
                        computer_list[randomindex]=random.nextInt(6) + 1
                    }
                }
            }else if(gap < 20){
                ComputerRandom()
                ComputerRandom()
            }else {
                //normal
                //normal
                ComputerRandom()
                for (count in  0 ..4){
                    var randomindex = random.nextInt(5) + 1
                    if (computer_list[randomindex] <= 2){
                        computer_list[randomindex]=random.nextInt(6) + 1
                    }
                }
            }
        }
    }


}