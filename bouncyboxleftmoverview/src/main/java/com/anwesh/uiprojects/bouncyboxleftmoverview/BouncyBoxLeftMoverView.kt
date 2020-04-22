package com.anwesh.uiprojects.bouncyboxleftmoverview

/**
 * Created by anweshmishra on 23/04/20.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.app.Activity
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF

val nodes : Int = 5
val boxes : Int = 4
val scGap : Float = 0.02f
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#4CAF50")
val backColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawBouncyBoxLeftMover(i : Int, scale : Float, gap : Float, size : Float, paint : Paint) {
    val sf : Float = scale.sinify()
    val sf1 : Float = sf.divideScale(0, 2)
    val sf2 : Float = sf.divideScale(1, 2)
    save()
    translate(i * gap + gap * sf2, 0f)
    drawRect(RectF(0f, 0f, size, size), paint)
    drawLine(0f, 0f, gap * (sf1 - sf2), 0f, paint)
    restore()
}


fun Canvas.drawBouncyBoxLeftMovers(scale : Float, w : Float, size : Float, paint : Paint) {
    paint.style = Paint.Style.STROKE
    val gap : Float = w / (boxes - 1)
    for (j in 0..(boxes - 1)) {
        drawBouncyBoxLeftMover(j, 0f, gap, size, paint)
    }
    paint.style = Paint.Style.FILL
    val scDiv : Double = 1.0 / (boxes - 1)
    val i : Int = Math.floor(scale.sinify() / scDiv).toInt()
    drawBouncyBoxLeftMover(i, scale, gap, size, paint)
}

fun Canvas.drawBBLMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    paint.color = foreColor
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    save()
    translate(0f, gap * (i + 1))
    drawBouncyBoxLeftMovers(scale, w, size, paint)
    restore()
}

class BouncyBoxLeftMoverView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}