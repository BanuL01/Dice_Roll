package com.example.dice_application

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
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
    var com_dice_list: MutableList<ImageView>? = null
    var target_score = 101
    var isTie=false
    var hard_level = false
    var isWinPopUp: String? = null;
    var throwRound = 0;

    lateinit var H_text : TextView;
    lateinit var C_text : TextView;
    lateinit var round_number : TextView;
    lateinit var throw_button : Button;
    lateinit var score : Button;
    lateinit var com_score_text : TextView;
    lateinit var user_score_text : TextView

    var dialog : Dialog? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        dialog = Dialog(this)

        win_count = intent.getIntExtra("win_count",0)
        lose_count = intent.getIntExtra("lose_count", 0)
        hard_level = intent.getBooleanExtra("hard_switch", false)

        val ts_input : TextView = findViewById(R.id.ts_value)
        val name_id : TextView = findViewById(R.id.user_text)
        target_score = Integer.parseInt(intent.getStringExtra("key"))
        ts_input.text = target_score.toString()
        val user_text = (intent.getStringExtra("key2"))
        name_id.text = user_text.toString()

        throw_button = findViewById(R.id.throw_button)
        score = findViewById(R.id.score)
        round_number = findViewById(R.id.round_num)
        H_text = findViewById(R.id.H)
        C_text = findViewById(R.id.C)
        H_text.setText("H : "+ win_count.toString())
        C_text.setText("C : "+ lose_count.toString())

        com_score_text = findViewById(R.id.com_score)
        user_score_text = findViewById(R.id.user_score)

        com_dice_list = mutableListOf(findViewById(R.id.com_d1),findViewById(R.id.com_d2),findViewById(R.id.com_d3),findViewById(R.id.com_d4),findViewById(R.id.com_d5)) // computer dice faces
        user_dice_List = mutableListOf(findViewById(R.id.user_d1),findViewById(R.id.user_d2),findViewById(R.id.user_d3),findViewById(R.id.user_d4),findViewById(R.id.user_d5)) // user dice faces


        if (savedInstanceState != null) {
            com_total = savedInstanceState.getInt("com_total")
            user_total = savedInstanceState.getInt("user_total")
            round_count = savedInstanceState.getInt("round_count")
            win_count=savedInstanceState.getInt("win_count")
            lose_count=savedInstanceState.getInt("lose_count")
            isTie= savedInstanceState.getBoolean("isTie")
            throwRound=savedInstanceState.getInt("throwRound")
            isWinPopUp=savedInstanceState.getString("isWinPopUp")

            user_list= savedInstanceState.getIntegerArrayList("user_list") as MutableList<Int>
            computer_list= savedInstanceState.getIntegerArrayList("computer_list") as MutableList<Int>
            var tempRemoveList=savedInstanceState.getIntegerArrayList("tempRemoveList") as MutableList<Int>

            for (index in 0..4){
                if (tempRemoveList[index]==0){
                    dice_select[index]=false
                }else{
                    dice_select[index]=true
                }
            }
        }
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
                    throwRound++
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
                throwRound++
            }
             else {
                throwRound=0
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
                setImage(com_dice_list!![diceIndex],computer_list[diceIndex])
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
                setImage(com_dice_list!![diceIndex],computer_list[diceIndex])
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
                isWinPopUp="win"
                dialog?.setContentView(R.layout.activity_result)
                dialog?.setCancelable(true)
                dialog?.setCanceledOnTouchOutside(false)
                dialog?.setOnCancelListener {
                    val intent = Intent(this, options::class.java)
                    intent.putExtra("win_count",win_count)
                    intent.putExtra("lose_count",lose_count)
                    startActivity(intent)
                }
                dialog?.show()
            } else if (user_total < com_total) {
                lose_count ++
                isWinPopUp="lost"
                dialog?.setContentView(R.layout.activity_result)
                val resultText = dialog?.findViewById<TextView>(R.id.win_text)
                if (resultText != null) {
                    resultText.text = "YOU LOST"
                }
                if (resultText != null) {
                    resultText.setTextColor(Color.RED)
                }
                dialog?.setCancelable(true)
                dialog?.setCanceledOnTouchOutside(false)
                dialog?.setOnCancelListener {
                    val intent = Intent(this, options::class.java)
                    intent.putExtra("win_count",win_count)
                    intent.putExtra("lose_count",lose_count)
                    startActivity(intent)
                }
                dialog?.show()

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("com_total", com_total)
        outState.putInt("user_total", user_total)
        outState.putInt("round_count", round_count)
        outState.putInt("win_count", win_count)
        outState.putInt("lose_count", lose_count)
        outState.putBoolean("isTie", isTie)
        outState.putIntegerArrayList("user_list", ArrayList(user_list))
        outState.putIntegerArrayList("computer_list", ArrayList(computer_list))

        outState.putInt("throwRound", throwRound)
        outState.putString("isWinPopUp", isWinPopUp)


        var tempRemoveList = mutableListOf<Int>()
        for (index in 0..4){
            if (dice_select[index]==true){
                tempRemoveList.add(1)
            }else{
                tempRemoveList.add(0)
            }
        }

        outState.putIntegerArrayList("tempRemoveList", ArrayList(tempRemoveList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        com_total = savedInstanceState.getInt("com_total")
        user_total = savedInstanceState.getInt("user_total")
        round_count = savedInstanceState.getInt("round_count")
        win_count=savedInstanceState.getInt("win_count")
        lose_count=savedInstanceState.getInt("lose_count")
        isTie= savedInstanceState.getBoolean("isTie")
        throwRound=savedInstanceState.getInt("throwRound")
        isWinPopUp=savedInstanceState.getString("isWinPopUp")

        user_list= savedInstanceState.getIntegerArrayList("user_list") as MutableList<Int>
        computer_list= savedInstanceState.getIntegerArrayList("computer_list") as MutableList<Int>
        var tempRemoveList=savedInstanceState.getIntegerArrayList("tempRemoveList") as MutableList<Int>

        for (index in 0..4){
            if (tempRemoveList[index]==0){
                dice_select[index]=false
            }else{
                dice_select[index]=true
            }
        }

        whenRotateSet()
    }

    private fun whenRotateSet() {
        com_score_text.setText(com_total.toString())
        user_score_text.setText(user_total.toString())
        round_number.setText(round_count.toString())

        if (!(computer_list.size == 0)) {
            //set images in screen
            for (diceIndex in 0..4) {
                setImage(com_dice_list!![diceIndex], computer_list[diceIndex])
                setImage(user_dice_List!![diceIndex], user_list[diceIndex])
            }
        }

        if (throwRound == 0) {
            throw_button.setText("Throw")
        } else if (throwRound == 1) {
            throw_button.setText("Rethrow")
        } else {
            throw_button.setText("Rethrow Again")
        }

        if (isTie || throwRound == 0) {
            score.isClickable = false
            //user image set touch false
            for (index in 0..4) {
                user_dice_List!![index].isClickable = false
            }
        } else {
            score.isVisible = true
            //user image set touch false
            for (index in 0..4) {
                user_dice_List!![index].isClickable = true
            }
        }

        for (imageIndex in 0 until 5) {
            if (dice_select[imageIndex] == false) {
                user_dice_List?.get(imageIndex)?.setBackgroundColor(Color.TRANSPARENT)
            } else {
                user_dice_List?.get(imageIndex)?.setBackgroundColor(getColor(R.color.brown_background))
            }
        }

        println(isWinPopUp)

        if (isWinPopUp == "win") {
            dialog?.setContentView(R.layout.activity_result)
            dialog?.setCancelable(true)
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setOnCancelListener {
                val intent = Intent(this, options::class.java)
                intent.putExtra("win_count", win_count)
                intent.putExtra("lose_count", lose_count)
                startActivity(intent)
            }
            dialog?.show()
        } else if (isWinPopUp == "lost") {
            dialog?.setContentView(R.layout.activity_result)
            val resultText = dialog?.findViewById<TextView>(R.id.win_text)
            if (resultText != null) {
                resultText.text = "YOU LOST"
            }
            resultText?.setTextColor(Color.RED)
            dialog?.setCancelable(true)
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setOnCancelListener {
                val intent = Intent(this, options::class.java)
                intent.putExtra("win_count", win_count)
                intent.putExtra("lose_count", lose_count)
                startActivity(intent)
            }
            dialog?.show()
        }
    }

    override fun onPause() {
        super.onPause()
        if (dialog != null && dialog!!.isShowing()) {
            dialog!!.dismiss()
        }
    }

}