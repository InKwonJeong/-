package com.example.android.swipebuttonexample

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins


class CharacterButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val slidingButton = ImageView(context)
    private val forwardImage = ImageView(context)
    private val backImage = ImageView(context)

    private var initialX = 0f
    private var enable = false

    private val buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_face_72dp)
    private val forwardDrawable =
        ContextCompat.getDrawable(context, R.drawable.ic_arrow_forward_24dp)
    private val backDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_24dp)

    var rightSwipeListener: (() -> Unit)? = null
    var leftSwipeListener: (() -> Unit)? = null

    init {
        slidingButton.background = ContextCompat.getDrawable(context, R.drawable.shape_button)
        slidingButton.setImageDrawable(buttonDrawable)

        val layoutParamsButton = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParamsButton.addRule(CENTER_IN_PARENT, TRUE)

        forwardImage.setImageDrawable(forwardDrawable)
        forwardImage.setPadding(40, 40, 40, 40)

        val layoutParamsForward = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParamsForward.addRule(ALIGN_PARENT_END, TRUE)
        layoutParamsForward.addRule(CENTER_VERTICAL, TRUE)
        layoutParamsForward.marginEnd = 20

        backImage.setImageDrawable(backDrawable)
        backImage.setPadding(40, 40, 40, 40)

        val layoutParamsBack = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParamsBack.addRule(ALIGN_PARENT_START, TRUE)
        layoutParamsBack.addRule(CENTER_VERTICAL, TRUE)
        layoutParamsBack.marginStart = 20

        addView(forwardImage, layoutParamsForward)
        addView(backImage, layoutParamsBack)
        addView(slidingButton, layoutParamsButton)

        setOnTouchListener(getButtonTouchListener())
    }

    private fun getButtonTouchListener(): OnTouchListener? {
        return OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 버튼 위를 터치하여 슬라이드할 때만 버튼을 이동시킨다
                    if (event.x >= slidingButton.x &&
                        event.x <= slidingButton.x + slidingButton.width)
                        enable = true
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (initialX == 0f) {
                        initialX = slidingButton.x
                    }

                    if (enable) {
                        // 버튼을 오른쪽으로 슬라이딩할 때 버튼의 위치와 텍스트 투명도를 변경한다
                        if (event.x - slidingButton.width / 2 > 0 &&
                            event.x + slidingButton.width / 2 < width) {
                            slidingButton.x = event.x - slidingButton.width / 2
                        }
                        // 터치가 오른쪽 경계 밖으로 나갔을 때 버튼을 레이아웃 오른쪽 끝에 위치시킨다
                        if (event.x + slidingButton.width / 2 > width) {
                            slidingButton.x = (width - slidingButton.width).toFloat()
                        }
                        // 터치가 왼쪽 경계 밖으로 나갔을 때 버튼을 레이아웃 왼쪽 끝에 위치시킨다
                        if (event.x - slidingButton.width / 2 < 0) {
                            slidingButton.x = 0f
                        }
                    }

                    true
                }
                MotionEvent.ACTION_UP -> {
                    when {
                        slidingButton.x + slidingButton.width > width - forwardImage.width / 2 -> {
                            slidingButton.x = (width - slidingButton.width).toFloat()
                            rightSwipeListener?.invoke()
                            moveButtonBack()
                        }
                        slidingButton.x < backImage.width / 2 -> {
                            slidingButton.x = 0f
                            leftSwipeListener?.invoke()
                            moveButtonBack()
                        }
                        else -> {
                            moveButtonBack()
                        }
                    }
                    enable = false
                    true
                }
                else -> false
            }
        }
    }

    private fun moveButtonBack() {
        val positionAnimator = ValueAnimator.ofFloat(slidingButton.x, initialX)
        positionAnimator.interpolator = AccelerateDecelerateInterpolator()
        positionAnimator.addUpdateListener {
            val x = positionAnimator.animatedValue as Float
            slidingButton.x = x
        }

        positionAnimator.duration = 200
        positionAnimator.start()
    }
}