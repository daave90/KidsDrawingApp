package com.example.kidsdrawingapp

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dravingView = findViewById<DrawingView>(R.id.drawing_view)
        dravingView.setSizeForBrush(20.toFloat())

        val brushBtn = findViewById<ImageButton>(R.id.ib_brush)
        brushBtn.setOnClickListener { showBrushSizeDialog() }
    }

    private fun showBrushSizeDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Choose brush size")

        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        val largeBtn = brushDialog.findViewById<ImageButton>(R.id.ib_large_brush)

        smallBtn.setOnClickListener { changeBrushSizeAndCloseDialog(10.0f, brushDialog) }
        mediumBtn.setOnClickListener { changeBrushSizeAndCloseDialog(20.0f, brushDialog) }
        largeBtn.setOnClickListener { changeBrushSizeAndCloseDialog(30.0f, brushDialog) }
        brushDialog.show()
    }

    private fun changeBrushSizeAndCloseDialog(size: Float, dialog: Dialog) {
        val drawingView = findViewById<DrawingView>(R.id.drawing_view)
        drawingView.setSizeForBrush(size)
        dialog.dismiss()
    }
}