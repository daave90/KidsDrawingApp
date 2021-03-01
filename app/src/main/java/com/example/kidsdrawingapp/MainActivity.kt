package com.example.kidsdrawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

        val galleryBtn = findViewById<ImageButton>(R.id.ib_image)
        galleryBtn.setOnClickListener {
            if(isReadStorageAllowed()) {
                pickupPhotoFromGallery()
            } else {
                requestStoragePermission()
            }
        }
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

    private fun requestStoragePermission() {
        if(shouldAskForExternalStoragePermission()) {
            Toast.makeText(this, "Need permission to add a background", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isReadStorageAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldAskForExternalStoragePermission(): Boolean{
        return ActivityCompat.shouldShowRequestPermissionRationale(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())
    }

    companion object{
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }

    private fun pickupPhotoFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
            try {
                if(data?.data != null) {
                    val imageBackground = findViewById<ImageView>(R.id.iv_background)
                    imageBackground.visibility = View.VISIBLE
                    imageBackground.setImageURI(data.data)
                } else {
                    Toast.makeText(this, "Error in parsing the image or its corrupted.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
