package com.example.kidsdrawingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dravingView = findViewById<DrawingView>(R.id.drawing_view)
        dravingView.setSizeForBrush(20.toFloat())
    }
}