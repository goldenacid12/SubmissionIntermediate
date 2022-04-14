package com.dicoding.latihan.submissionintermediate

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class MyButton : AppCompatButton {

    private lateinit var enabledBackground: Drawable

    // Konstruktor dari MyButton
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    // Metode onDraw() digunakan untuk mengcustom button ketika enable dan disable
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Mengubah background dari Button
        background = enabledBackground

        // Mengubah ukuran text pada button
        textSize = 12f

        // Menjadikan object pada button menjadi center
        gravity = Gravity.CENTER

        // Mengubah text pada button pada kondisi enable dan disable
        text = context.getString(R.string.log_out)
    }

    // pemanggilan Resource harus dilakukan saat kelas MyButton diinisialisasi, jadi harus dikeluarkan dari metode onDraw
    private fun init() {
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }
}