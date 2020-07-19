package com.jay.android.work.loadanimator.bean

import android.graphics.Paint
import android.graphics.Path

class LoadAnimatorBean(mDefRadioY: Int) {
    lateinit var paint: Paint
    lateinit var path: Path
    var ballRadio:Float = 0f
    var radioX: Double = 0.0
    var radioY: Double = mDefRadioY.toDouble()
}