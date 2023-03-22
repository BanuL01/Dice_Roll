package com.example.dice_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar

// video demonstration link - https://drive.google.com/drive/folders/1zxvP7r-tBn5oxLoXrOT63Jjg3HAb1Sen?usp=sharing
// w1867098 - Banuji Liyanwala
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loading = findViewById<ProgressBar>(R.id.loadingBar)

        object : CountDownTimer(2500, 1000) { // 10 seconds countdown with 1 second interval
            override fun onTick(millisUntilFinished: Long) {
                val progress = (2500 - millisUntilFinished) / 20 // calculate the progress percentage
                loading.progress = progress.toInt()
            }

            override fun onFinish() {
                // do something when the countdown is finished
            }
        }.start()




        // Next page connect
        //        //source - https://www.geeksforgeeks.org/how-to-create-a-splash-screen-in-android-using-kotlin/
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, options::class.java)
            startActivity(intent)
            finish()
        },2500)

    }
}