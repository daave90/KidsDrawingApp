package com.example.kidsdrawingapp

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dev.sasikanth.colorsheet.ColorSheet

class MainActivity : AppCompatActivity() {
    private var selectedColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectedColor = ContextCompat.getColor(this, R.color.black)

        val dravingView = findViewById<DrawingView>(R.id.drawing_view)
        dravingView.setSizeForBrush(20.toFloat())

        val brushBtn = findViewById<ImageButton>(R.id.ib_brush)
        brushBtn.setOnClickListener { showBrushSizeDialog() }

        val colorPickerBtn = findViewById<ImageButton>(R.id.ib_color)
        colorPickerBtn.setOnClickListener { showColorPickerDialog() }
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

    private fun showColorPickerDialog() {
        val colors = getSupportedColors();
        ColorSheet().colorPicker(
            colors = colors,
            listener = { color -> setUpPickedUpColor(color)},
            selectedColor = selectedColor
        ).show(supportFragmentManager)
    }

    private fun getSupportedColors(): IntArray {
        return intArrayOf(
            getColorIdFromResource(R.color.beige),
            getColorIdFromResource(R.color.black),
            getColorIdFromResource(R.color.red),
            getColorIdFromResource(R.color.green),
            getColorIdFromResource(R.color.blue),
            getColorIdFromResource(R.color.teal_700),
            getColorIdFromResource(R.color.purple_500),
            getColorIdFromResource(R.color.yellow)
        )
    }

    private fun getColorIdFromResource(colorResource: Int): Int {
        return ContextCompat.getColor(this, colorResource)
    }

    private fun setUpPickedUpColor(color: Int) {
        selectedColor = color
        val drawingView = findViewById<DrawingView>(R.id.drawing_view)
        drawingView.setColor(color)
    }
}
