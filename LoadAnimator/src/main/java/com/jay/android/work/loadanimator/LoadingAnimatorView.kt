package com.jay.android.work.loadanimator

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.core.graphics.createBitmap
import com.jay.android.work.loadanimator.bean.LoadAnimatorBean
import kotlin.math.cos
import kotlin.math.sin

class LoadingAnimatorView: View {
    /**
     * 设置默认尺寸大小
     */
    private val mDefaultSize: Int = 300

    /**
     * 设置动画旋转半径
     */
    private val mLoadingRadio: Int = 100

    /**
     * 设置动画球的默认个数
     */
    private val mLoadBallSize = 7

    /**
     * 小球集合列表
     */
    private val mLoadBallList: SparseArray<LoadAnimatorBean> = SparseArray()

    /**
     * 小球默认颜色
     */
    private var mColor: Int = Color.GRAY

    /**
     * 小球的最大半径
     */
    private var mBallMaxRadio = 40

    /**
     * 小球的半径偏移量
     */
    private var mBallRadioOffset = 5

    private val mPaint: Paint = Paint()

    private lateinit var valueAnimator: ValueAnimator

    constructor(context: Context):this(context,null,0)
    constructor(context: Context,attributeSet:AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context,attributeSet: AttributeSet?,defStyle: Int):super(context,attributeSet,defStyle){
        init(context,attributeSet)
    }

    private var ballAngle = 0
    private var ballAngle1 = 330
    private var ballAngle2 = 300
    private var ballAngle3 = 270

    @SuppressLint("HandlerLeak")
    /*inner class LoadingHandler : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            //更新球的旋转角度
            ballAngle += 30
            ballAngle1 += 30
            ballAngle2 += 30
            ballAngle3 += 30
            if (ballAngle >= 360){
                ballAngle = 0
            }
            if (ballAngle1 >= 360){
                ballAngle1 = 0
            }
            if (ballAngle2 >= 360){
                ballAngle2 = 0
            }
            if (ballAngle3 >= 360){
                ballAngle3 = 0
            }
            invalidate()
            loadHandler.sendEmptyMessageDelayed(0,100)
            loadHandler.removeMessages(0)
        }
    }*/

//    val loadHandler:LoadingHandler = LoadingHandler()

    lateinit var mCanvas: Canvas
    lateinit var bitmap:Bitmap

    /**
     * 初始化
     */
    private fun init(context: Context, attributeSet: AttributeSet?) {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.LoadingAnimatorView)
        typedArray.recycle()

        mPaint.apply {
            style = Paint.Style.FILL
            color = mColor
            isAntiAlias = true
        }

//        loadHandler.sendEmptyMessageDelayed(0,100)

        val valueAnimator = ValueAnimator.ofInt(0, 11)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener {
            val animatedValue = it.animatedValue
            Log.e("tag","animatedValue:$animatedValue")
            //更新球的旋转角度
            ballAngle += 30
            ballAngle1 += 30
            ballAngle2 += 30
            ballAngle3 += 30
            if (ballAngle >= 360){
                ballAngle = 0
            }
            if (ballAngle1 >= 360){
                ballAngle1 = 0
            }
            if (ballAngle2 >= 360){
                ballAngle2 = 0
            }
            if (ballAngle3 >= 360){
                ballAngle3 = 0
            }
            postInvalidate()
        }

        valueAnimator.repeatCount = Animation.INFINITE
        valueAnimator.duration = 2500
        valueAnimator.start()
    }

    private fun drawBack() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444)
        Log.e("tag","drawBack--------------> ${bitmap.width}         ${bitmap.height}")
        mCanvas = Canvas(bitmap)
        mCanvas.drawColor(Color.YELLOW)
        mCanvas.translate(width / 2f,height / 2f)
        for (i in 0..12) {
            //计算小球的坐标  注意点，此处三角函数传的是弧度，而不是角度
            val ballX = mLoadingRadio * cos((i * 30 * Math.PI / 180))
            val ballY = mLoadingRadio * sin((i * 30 * Math.PI / 180))
            mPath.addCircle(ballX.toFloat(), ballY.toFloat(), 15f, Path.Direction.CCW)
            mCanvas.drawPath(mPath, mPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(getSize(widthMeasureSpec),getSize(heightMeasureSpec))
    }

    private fun getSize(measureSpec:Int) = when(MeasureSpec.getMode(measureSpec)){
        MeasureSpec.UNSPECIFIED -> MeasureSpec.getSize(measureSpec)
        MeasureSpec.AT_MOST -> mDefaultSize
        else -> MeasureSpec.getSize(measureSpec)
    }

    private val mPath = Path()
    private val path = Path()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GREEN
        isAntiAlias = true
    }
    private var isDraw = true
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        Log.e("tag","draw...........$width     $height")
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("tag","onDraw start......canvas:$canvas")
        canvas?.translate(width / 2f, height / 2f)
        if (isDraw){
            drawBack()
            isDraw = false
        }
        //绘制背景球
        /*for (i in 0..12) {
            //计算小球的坐标  注意点，此处三角函数传的是弧度，而不是角度
            val ballX = mLoadingRadio * cos((i * 30 * Math.PI / 180))
            val ballY = mLoadingRadio * sin((i * 30 * Math.PI / 180))
            mPath.addCircle(ballX.toFloat(), ballY.toFloat(), 15f, Path.Direction.CCW)
            canvas?.drawPath(mPath, mPaint)
        }*/
        canvas?.drawBitmap(bitmap,-width/2f,-height/2f,mPaint)

        //绘制旋转白球
        path.reset()
        val ballX = mLoadingRadio * cos((ballAngle * Math.PI / 180))
        val ballY = mLoadingRadio * sin((ballAngle * Math.PI / 180))
        path.addCircle(ballX.toFloat(),ballY.toFloat(),25f,Path.Direction.CCW)

        val ballX1 = mLoadingRadio * cos((ballAngle1 * Math.PI / 180))
        val ballY1 = mLoadingRadio * sin((ballAngle1 * Math.PI / 180))
        path.addCircle(ballX1.toFloat(),ballY1.toFloat(),22f,Path.Direction.CCW)

        val ballX2 = mLoadingRadio * cos((ballAngle2 * Math.PI / 180))
        val ballY2 = mLoadingRadio * sin((ballAngle2 * Math.PI / 180))
        path.addCircle(ballX2.toFloat(),ballY2.toFloat(),20f,Path.Direction.CCW)

        val ballX3 = mLoadingRadio * cos((ballAngle3 * Math.PI / 180))
        val ballY3 = mLoadingRadio * sin((ballAngle3 * Math.PI / 180))
        path.addCircle(ballX3.toFloat(),ballY3.toFloat(),18f,Path.Direction.CCW)
        canvas?.drawPath(path,paint)
        Log.e("tag","onDraw......end")
    }

}