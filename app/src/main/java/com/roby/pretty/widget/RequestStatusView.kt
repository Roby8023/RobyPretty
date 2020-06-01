package com.roby.pretty.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
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
    private val textPaint: TextPaint by lazy { TextPaint() } // 文字画笔
    private var statusImage: Drawable? = null // Status icon
    private var totalStep: Int = 3 // 总共step数
    private var currentStep: Int = 1 // 当前step数
    private var normalBg: Int = Color.BLUE //未选中Line背景
    private var selectedBg: Int = Color.CYAN // 选中Line画笔
    private var pointRadius: Float = 10F // 未选中Point半径
    private val viewYPosition = 50F // 控件中心线在Y轴高度
    private val statusViewConfig = 36 // Status icon尺寸
    private val lineWidth = 5F // 线宽度
    private val textYPosition = viewYPosition + 22.dpToPx() // 文字Y轴位置
    private var statusIconBitmap: Bitmap? = null // statusImage 转换过来的 Bitmap
    private var stepOneText = "step 1" // step 1 文字
    private var stepTwoText = "step 2" // step 2 文字
    private var stepThreeText = "step 3" // step 3 文字
    private var layout: StaticLayout? = null
    private var textColor = "#0000ff"
    private var tintColor: Int = 0

    init {
        processAttributes(attrs, defStyleAttr)
        initView()
    }

    private fun initView() {
        whitePaint.color = Color.WHITE
        whitePaint.alpha = 255
        whitePaint.isAntiAlias = true
        normalPaint.color = normalBg
        normalPaint.strokeWidth = lineWidth
        normalPaint.textSize = 16.dpToPx().toFloat()
        normalPaint.isAntiAlias = true
        normalPaint.textAlign = Paint.Align.CENTER
        selectedPaint.color = selectedBg
        selectedPaint.strokeWidth = lineWidth
        selectedPaint.isAntiAlias = true
        statusIconBitmap =
            statusImage?.toBitmap(statusViewConfig.dpToPx(), statusViewConfig.dpToPx())

        textPaint.color = Color.parseColor(textColor)
        textPaint.isAntiAlias = true
        textPaint.textSize = 14.dpToPx().toFloat()
        textPaint.textAlign = Paint.Align.CENTER
//        textPaint.
    }

    private fun processAttributes(attrs: AttributeSet?, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.RequestStatusView, 0, defStyleRes).apply {
            statusImage = getDrawable(R.styleable.RequestStatusView_statusImage)
            totalStep = getInt(R.styleable.RequestStatusView_totalStep, 3)
            currentStep = getInt(R.styleable.RequestStatusView_currentStep, 1)
            normalBg = getColor(R.styleable.RequestStatusView_normalBg, Color.BLUE)
            selectedBg = getColor(R.styleable.RequestStatusView_selectedBg, Color.YELLOW)
            stepOneText = getString(R.styleable.RequestStatusView_stepOneText).orEmpty()
            stepTwoText = getString(R.styleable.RequestStatusView_stepTwoText).orEmpty()
            stepThreeText = getString(R.styleable.RequestStatusView_stepThreeText).orEmpty()
            tintColor = getColor(R.styleable.RequestStatusView_android_tint, 0)
            setIconTintColor(getColor(R.styleable.RequestStatusView_android_tint, 0))
//        setIconTintColor(
//                mTypeArray.getColor(
//                        R.styleable.RequestStatusView_android_tint,
//                        0
//                )
//        )
            recycle()
        }
    }

    private fun setIconTintColor(@ColorInt tint: Int) {
        Log.e("RobyFlag", "tint: $tint")
        statusImage?.let { drawable ->
            tint.takeUnless { it == 0 }
                ?.let { DrawableCompat.setTint(drawable, it) }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val viewStartPoint = getScreenWidth() / 6
        val viewTotalLength = getScreenWidth() * 2 / 3
        val viewChildLineLength = viewTotalLength / (totalStep - 1) // 单个step view line的长度
        // first step
        canvas?.drawCircle(viewStartPoint, viewYPosition, pointRadius, normalPaint)
//        canvas?.drawText(
//            stepOneText,
//            viewStartPoint,
//            textYPosition,
//            normalPaint
//        )

        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        layout = StaticLayout(
            stepOneText, textPaint, 300,
            Layout.Alignment.ALIGN_NORMAL, 1F, 0.0F, true
        )
        canvas?.save()
        canvas?.translate(viewStartPoint, textYPosition)//从100，100开始画
        layout?.draw(canvas)
        canvas?.restore()//别忘了restore

        for (index in 0 until totalStep - 1) {
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

            // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
            layout = StaticLayout(
                when (index) {
                    0 -> stepTwoText
                    1 -> stepThreeText
                    else -> "666"
                }, textPaint,
                when (index) {
                    0 -> 600
                    1 -> 300
                    else -> 300
                },
                Layout.Alignment.ALIGN_NORMAL, 1F, 0.0F, true
            )
            canvas?.save()
            canvas?.translate(
                viewStartPoint + viewChildLineLength * (index + 1),
                textYPosition
            )
            layout?.draw(canvas)
            canvas?.restore()
        }

        /** set current step */
        if (currentStep > totalStep) currentStep = 1

        when (currentStep) {
            1 -> {
                // draw the pic on the first step, and no need to draw line bg
                statusImage?.let {
                    // for switching app
                    if (statusIconBitmap?.isRecycled == true) {
                        statusIconBitmap = statusImage?.toBitmap(
                            statusViewConfig.dpToPx(),
                            statusViewConfig.dpToPx()
                        )
                    }
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
                for (index in 0 until currentStep - 1) {
                    canvas?.drawLine(
                        viewStartPoint + viewChildLineLength * index, // + pointRadius.halfOf().toFloat(),  // view起点坐标 + point半径
                        viewYPosition,
                        viewStartPoint + viewChildLineLength * (index + 1),
                        viewYPosition,
                        selectedPaint
                    )
                    // 最后一次循环时画picture, 其余画point
                    if (index == currentStep - 2) {
                        canvas?.drawCircle(
                            viewStartPoint + viewChildLineLength * (index + 1),
                            viewYPosition,
                            pointRadius,
                            whitePaint
                        )
                        statusImage?.let {
                            // for switching app
                            if (statusIconBitmap?.isRecycled == true) {
                                statusIconBitmap = statusImage?.toBitmap(
                                    statusViewConfig.dpToPx(),
                                    statusViewConfig.dpToPx()
                                )
                            }
                            statusIconBitmap?.let {
//                                val iconBitWidth = it.width.toFloat() ?: 100F
                                val iconBitHeight = it.height.toFloat()
                                selectedPaint.colorFilter = ColorFilter()
                                canvas?.drawBitmap(
                                    it,
                                    viewStartPoint + viewChildLineLength * (index + 1) - statusViewConfig.dpToPx() / 2,
                                    viewYPosition - iconBitHeight / 2,
                                    selectedPaint
                                )
                            }
                        } ?: run {
                        }
                    } else {
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