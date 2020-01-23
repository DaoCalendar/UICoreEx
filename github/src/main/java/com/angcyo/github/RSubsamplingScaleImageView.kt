package com.angcyo.github

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

/**
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/23
 */
class RSubsamplingScaleImageView : SubsamplingScaleImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet?) : super(context, attr)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(event)
    }
}

/**设置采样图片*/
fun SubsamplingScaleImageView.setImage(filePath: String) {
    setImage(ImageSource.uri(filePath))
}