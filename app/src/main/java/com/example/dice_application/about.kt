package com.example.dice_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class about : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_about)

    }
}