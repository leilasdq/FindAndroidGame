package com.example.findmegame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import java.util.*
import kotlin.math.floor

class SpotLight @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var shader: BitmapShader
    private var paint = Paint()
    private var shouldDrawSpotLight = false
    private var gameOver = false

    private lateinit var winnerRect: RectF
    private var androidBitmapX = 0f
    private var androidBitmapY = 0f

    private lateinit var bitmap: Bitmap

    private val shaderMatrix = Matrix()

    private val bitmapAndroid = BitmapFactory.decodeResource(
        resources,
        R.drawable.android
    )
    private val spotlight = BitmapFactory.decodeResource(resources, R.drawable.mask)

    init {
        val bitmap = Bitmap.createBitmap(spotlight.width, spotlight.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val shaderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        shaderPaint.color = Color.BLACK
        canvas.drawRect(0.0f, 0.0f, spotlight.width.toFloat(), spotlight.height.toFloat(), shaderPaint)

        shaderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawBitmap(spotlight, 0f, 0f, shaderPaint)

        shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(Color.CYAN)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas?.drawBitmap(bitmapAndroid, androidBitmapX, androidBitmapY, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        shaderMatrix.setTranslate(
            (event?.x?.minus(spotlight.width / 2.0f) ?: 0f),
            (event?.y?.minus(spotlight.width / 2.0f) ?: 0f)
        )
        shader.setLocalMatrix(shaderMatrix)
        invalidate()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupWinnerRect()
    }

    private fun setupWinnerRect() {
        androidBitmapX =
            floor((Random().nextFloat() * (width - bitmapAndroid.width)).toDouble()).toFloat()
        androidBitmapY =
            floor((Random().nextFloat() * (height - bitmapAndroid.height)).toDouble()).toFloat()

        winnerRect = RectF(
            (androidBitmapX),
            (androidBitmapY),
            (androidBitmapX + bitmapAndroid.width),
            (androidBitmapY + bitmapAndroid.height)
        )
    }
}