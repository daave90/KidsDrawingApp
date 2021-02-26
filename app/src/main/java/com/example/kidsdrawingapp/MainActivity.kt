package com.example.kidsdrawingapp

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var selectedBrushColorBtn: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dravingView = findViewById<DrawingView>(R.id.drawing_view)
        dravingView.setSizeForBrush(20.toFloat())

        val brushBtn = findViewById<ImageButton>(R.id.ib_brush)
        brushBtn.setOnClickListener { showBrushSizeDialog() }

        selectedBrushColorBtn = findViewById<LinearLayout>(R.id.ll_paint_colors)[1] as ImageButton
        selectedBrushColorBtn?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )
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