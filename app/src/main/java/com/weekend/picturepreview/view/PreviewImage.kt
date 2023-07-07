package com.weekend.picturepreview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import kotlin.math.abs
import com.github.chrisbanes.photoview.PhotoView

class PreviewImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PhotoView(context, attrs, defStyleAttr) {

    // 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    private var x1 = 0f
    private var x2 = 0f
    private var y1 = 0f
    private var y2 = 0f

    // 是否拦截事件
    private var intercept = false

    // 透明度回调
    private var mAlphaCallback: AlphaCallback? = null

    // 用于记录当前图片所在位置
    private var locationX = 0
    private var locationY = 0

    // 用于记录透明度
    private var mAlpha = 1.0f

    // up事件时候记录图片的偏移位置，用于触发单击回调的时候判断，区分单击和短距离快速滑动
    var upLocationX = 0

    //一些可选配置 {
    // 图片可缩小的最小比例
    private val photoMinScale = 1.0f
    // 是否单击关闭预览页面
    private var isClickClose =true
    // 向下滑的时候是否将图片大小变小
    private var isSlideChangeSize = false
    // 滑动距离达到屏幕的几分之一则飞回。
    private val FLING_RATIO = 3
    // 单指拖动的时候图片运动轨迹: true:只纵向移动; false:跟随手指随意移动
    private val isSlideOnlyVertical = true
    //一些可选配置 }

    init {
        // 设置最小比例
        minimumScale = photoMinScale
        setOnClickListener {
            if(isClickClose){
                if (abs(upLocationX) <5) mAlphaCallback?.onChangeClose()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    // 这里重写onTouchEvent没有效果，因为PhotoView需要设置onTouchEvent，这样会造成冲突
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            // 按下
            MotionEvent.ACTION_DOWN -> {
                // 拦截viewpager2的事件
                parent?.requestDisallowInterceptTouchEvent(true)
                y1 = event.y
                x1 = event.x
                attacher.onTouch(this, event)
            }
            MotionEvent.ACTION_UP -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                y2 = event.y
                x2 = event.x
                upLocationX = locationX
                ObjectAnimator.ofInt(this, "locationX", if(isSlideOnlyVertical) 0 else locationX, 0).start()
                ObjectAnimator.ofInt(this, "locationY", locationY, 0).start()

                //若图片向下滑动规定的距离就关闭预览
                if ((locationY < (0 - height / FLING_RATIO)) && (event.pointerCount == 1)) {
                    mAlphaCallback?.onChangeClose()
                }

                // 如果是手动放大，不做复原，缩小则会复原
                if (scale <= 1.0f) {
                    //PhotoView的bug，虽设置了最小scale，但是还是可以强制缩小到小于scale一点点的大小，
                    //当复原如果是使用动画的话，会设置scale从低于最小scale开始从而会抛出异常，
                    //当前需求不需要缩小，因此可通过直接恢复的方式避开动画的异常
                    scale = 1.0f
                    //ObjectAnimator.ofFloat(this, "scale", scale, 1.0f).start()
                }
                ObjectAnimator.ofFloat(this, "malpha", mAlpha, 1.0f).start()
                attacher.onTouch(this, event)
                intercept = false
            }
            MotionEvent.ACTION_MOVE -> {
                y2 = event.y
                x2 = event.x
                // 判断为滑动事件 并且需要在1个手指头的时候触发
                if (abs(y1 - y2) > ViewConfiguration.get(context).scaledTouchSlop && event.pointerCount == 1 && scale <= 1.0f) {
                    intercept = true
                    moveAndScale(event, x1 - x2, y1 - y2)
                    return true
                } else if (event.pointerCount > 1 || abs(x1 - x2) > ViewConfiguration.get(context).scaledTouchSlop) {
                    if (!intercept)
                        attacher.onTouch(this, event)
                } else if (scale > 1.0f) {
                    attacher.onTouch(this, event)
                }
            }
        }

        return true
    }

    private fun setMalpha(alpha: Float) {
        mAlpha = alpha
        mAlphaCallback?.onChangeAlphaCallback(alpha)
    }

    // 用于动画设置的属性
    private fun setLocationX(x: Int) {
        locationX = x
        scrollTo(locationX, locationY)
    }

    // 用于动画设置的属性
    private fun setLocationY(y: Int) {
        locationY = y
    }

    // 这里是处理移动、放大缩小、透明度的关键代码
    private fun moveAndScale(event: MotionEvent, fl: Float, fl1: Float): Boolean {
        locationX = fl.toInt()
        locationY = fl1.toInt()
        scrollTo(if(isSlideOnlyVertical) 0 else locationX, locationY)

        // 手指滑动的时候，改变背景透明度和图片大小
        // 滑动比例
        var fl2 = abs(locationY).toFloat()
        var cale = (height/2 - fl2) / (height / 2)
        if (cale <= 0.3f) cale = 0.3f
        if(isSlideChangeSize) scale = cale
        setMalpha(cale)

        return false
    }

    /**
     * 设置透明度回调
     * @param mAlphaCallback
     */
    fun setAlphaCallback(mAlphaCallback: AlphaCallback) {
        this.mAlphaCallback = mAlphaCallback
    }
}

/**
 * 回调
 */
interface AlphaCallback {
    // 透明度改变回调
    fun onChangeAlphaCallback(alpha: Float)
    // 关闭预览回调
    fun onChangeClose()
}