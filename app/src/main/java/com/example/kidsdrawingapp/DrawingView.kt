package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()
    private val mUndoPaths = ArrayList<CustomPath>()

    internal inner class CustomPath(
        var color: Int,
        var brushThickness: Float
    ) : Path() {}

    init {
        setUpDrawing()
    }

    fun setSizeForBrush(newSize: Float) {
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            newSize, resources.displayMetrics)
        mDrawPaint?.strokeWidth = mBrushSize
    }

    fun setColor(newColor: Int) {
        this.color = newColor
        mDrawPaint?.color = color
    }

    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint?.color = color
        mDrawPaint?.style = Paint.Style.STROKE
        mDrawPaint?.strokeJoin = Paint.Join.ROUND
        mDrawPaint?.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        for (path in mPaths) {
            mDrawPaint?.strokeWidth = path.brushThickness
            mDrawPaint?.color = path.color
            canvas?.drawPath(path, mDrawPaint!!)
        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint?.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint?.color = mDrawPath!!.color
            canvas?.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> setDrawingStartPoint(touchX, touchY)
            MotionEvent.ACTION_MOVE -> drawLine(touchX, touchY)
            MotionEvent.ACTION_UP -> restartDrawPath()
            else -> return false
        }
        invalidate()
        return true
    }

    fun undo() {
        if(mPaths.size > 0) {
            mUndoPaths.add(mPaths.removeAt(mPaths.size - 1))
            invalidate()
        }
    }

    private fun setDrawingStartPoint(touchX: Float?, touchY: Float?) {
        mDrawPath?.color = color
        mDrawPath?.brushThickness = mBrushSize
        mDrawPath?.reset()
        if (touchX != null && touchY != null) {
            mDrawPath?.moveTo(touchX, touchY)
        }
    }

    private fun drawLine(touchX: Float?, touchY: Float?) {
        if (touchX != null && touchY != null) {
            mDrawPath?.lineTo(touchX, touchY)
        }
    }

    private fun restartDrawPath() {
        mPaths.add(mDrawPath!!)
        mDrawPath = CustomPath(color, mBrushSize)
    }
}
