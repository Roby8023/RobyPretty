package com.roby.pretty.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import com.roby.pretty.R
import com.roby.pretty.ext.dpToPx


/**
 * Send Request status view
 * Created by Roby on 2020/5/29 23:49
 */
class RequestStatusView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val whitePaint: Paint by lazy { Paint() } // 白色画笔
    private val normalPaint: Paint by lazy { Paint() } // 未选中Line画笔
    private val selectedPaint: Paint by lazy { Paint() } // 选中Line画笔
    private var statusImage: Drawable? = null // Status icon
    private var totalStep: Int = 3 // 总共step数
    private var currentStep: Int = 1 // 当前step数
    private var normalBg: Int = Color.BLUE //未选中Line背景
    private var selectedBg: Int = Color.CYAN // 选中Line画笔
    private var pointRadius: Float = 20F // 未选中Point半径
    private val viewYPosition = 200F // 控件中心线在Y轴高度
    private val statusViewConfig = 25 // Status icon尺寸
    private val lineWidth = 10F // 线宽度
    private var statusIconBitmap: Bitmap? = null // statusImage 转换过来的 Bitmap

    init {
        processAttributes(attrs, defStyleAttr)
        initView()
    }

    private fun initView() {
        whitePaint.color = Color.WHITE
        whitePaint.alpha = 255
        normalPaint.color = normalBg
        normalPaint.strokeWidth = lineWidth
        selectedPaint.color = selectedBg
        selectedPaint.strokeWidth = lineWidth
        statusIconBitmap =
            statusImage?.toBitmap(statusViewConfig.dpToPx(), statusViewConfig.dpToPx())
    }

    private fun processAttributes(attrs: AttributeSet?, defStyleRes: Int) {
        val mTypeArray =
            context.obtainStyledAttributes(attrs, R.styleable.RequestStatusView, 0, defStyleRes)
        statusImage = mTypeArray.getDrawable(R.styleable.RequestStatusView_statusImage)
        totalStep = mTypeArray.getInt(R.styleable.RequestStatusView_totalStep, 3)
        currentStep = mTypeArray.getInt(R.styleable.RequestStatusView_currentStep, 1)
        normalBg = mTypeArray.getColor(R.styleable.RequestStatusView_normalBg, Color.BLUE)
        selectedBg = mTypeArray.getColor(R.styleable.RequestStatusView_selectedBg, Color.YELLOW)
        setIconTintColor(
            mTypeArray.getColor(
                R.styleable.RequestStatusView_android_tint,
                Color.CYAN
            )
        )
        mTypeArray.recycle()
    }

    private fun setIconTintColor(@ColorInt tint: Int) {
        statusImage?.let { drawable ->
            tint.takeUnless { it == 0 }
                ?.let { DrawableCompat.setTint(drawable, it) }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val viewStartPoint = getScreenWidth() / 6
        val viewTotalLength = getScreenWidth() * 2 / 3
        val viewChildLineLength = viewTotalLength / (totalStep - 1) // 单个step view line的长度
        Log.e("RobyFlag", "totalStep - $totalStep")
        Log.e("RobyFlag", "getScreenWidth() - ${getScreenWidth()}")
        Log.e("RobyFlag", "viewStartPoint - $viewStartPoint")
        Log.e("RobyFlag", "viewTotalLength - $viewTotalLength")
        Log.e("RobyFlag", "viewChildLineLength - $viewChildLineLength")
        // first step
        canvas?.drawCircle(viewStartPoint, viewYPosition, pointRadius, normalPaint)
        for (index in 0 until totalStep - 1) {
            Log.e("RobyFlag", "for循环 $index")
            canvas?.drawLine(
                viewStartPoint + viewChildLineLength * index, // + pointRadius.halfOf().toFloat(),  // view起点坐标 + point半径
                viewYPosition,
                viewStartPoint + viewChildLineLength * (index + 1),
                viewYPosition,
                normalPaint
            )
            canvas?.drawCircle(
                viewStartPoint + viewChildLineLength * (index + 1),
                viewYPosition,
                pointRadius,
                normalPaint
            )
        }

        /** set current step */
        if (currentStep > totalStep) currentStep = 1

        when (currentStep) {
            1 -> {
                // draw the pic on the first step, and no need to draw line bg
                statusImage?.let {
                    Log.e("RobyFlag", "step 1, pic ok")
                    statusIconBitmap?.let {
                        val iconBitWidth = it.width.toFloat()
                        val iconBitHeight = it.height.toFloat()
                        canvas?.drawCircle(
                            viewStartPoint,
                            viewYPosition,
                            iconBitWidth / 2,
                            whitePaint
                        )
                        canvas?.drawBitmap(
                            it,
                            viewStartPoint - iconBitWidth / 2,
                            viewYPosition - iconBitHeight / 2,
                            selectedPaint
                        )
                    }
                } ?: run {
                    Log.e("RobyFlag", "step 1, empty pic")
                }
            }
            else -> {
                // first step
                canvas?.drawCircle(
                    viewStartPoint,
                    viewYPosition,
                    pointRadius,
                    selectedPaint
                )
                Log.e("RobyFlag", ">> currentStep: $currentStep")
                for (index in 0 until currentStep - 1) {
                    Log.e("RobyFlag", "画图片 for循环 $index")
                    canvas?.drawLine(
                        viewStartPoint + viewChildLineLength * index, // + pointRadius.halfOf().toFloat(),  // view起点坐标 + point半径
                        viewYPosition,
                        viewStartPoint + viewChildLineLength * (index + 1),
                        viewYPosition,
                        selectedPaint
                    )
                    // 最后一次循环时画picture, 其余画point
                    if (index == currentStep - 2) {
                        Log.e("RobyFlag", "画图片  ==-1")
                        canvas?.drawCircle(
                            viewStartPoint + viewChildLineLength * (index + 1),
                            viewYPosition,
                            pointRadius,
                            whitePaint
                        )
                        statusImage?.let {
                            Log.e("RobyFlag", "画图片  pic ok")
                            statusIconBitmap?.let {
//                                val iconBitWidth = it.width.toFloat() ?: 100F
                                val iconBitHeight = it.height.toFloat()
                                canvas?.drawBitmap(
                                    it,
                                    viewStartPoint + viewChildLineLength * (index + 1) - statusViewConfig.dpToPx() / 2,
                                    viewYPosition - iconBitHeight / 2,
                                    selectedPaint
                                )
                            }
                        } ?: run {
                            Log.e("RobyFlag", "画图片   empty pic")
                        }
                    } else {
                        Log.e("RobyFlag", "画图片  !=-1")
                        canvas?.drawCircle(
                            viewStartPoint + viewChildLineLength * (index + 1),
                            viewYPosition,
                            pointRadius,
                            selectedPaint
                        )
                    }
                }
            }
        }
        statusIconBitmap?.recycle()
    }

    private fun getScreenWidth(): Float {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels.toFloat()
    }
}