package com.weekend.picturepreview.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.weekend.picturepreview.R
import java.util.ArrayList

class PhotoPreviewDialog : DialogFragment(), AlphaCallback {
    companion object {
        const val TAG: String = "PhotoPreviewDialog"
        fun newInstance(photoList: ArrayList<String>, index: Int): PhotoPreviewDialog {
            return PhotoPreviewDialog().apply {
                arguments = Bundle().apply {
                    putStringArrayList("photoList",photoList)
                    putInt("index", index)
                }
            }
        }
    }

    // 封装好的图片预览控件
    private var mPreviewPictureView :PreviewPictureView<String> ? =null
    private val index by lazy { arguments?.getInt("index") ?: 0 }
    private val photoList by lazy { arguments?.getStringArrayList("photoList") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.picture_preview_default_dialog)
        isCancelable = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.picture_preview_dialog, container, false);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 初始化预览控件
        mPreviewPictureView= view?.findViewById(R.id.pp_picture)

        // 开始预览
        photoList?.let { mPreviewPictureView?.start(index, it,this) }

    }

    // 改变透明度的动画回调
    override fun onChangeAlphaCallback(alpha: Float) {
        view?.findViewById<View>(R.id.view)?.alpha=alpha
    }

    // 触发关闭的回调
    override fun onChangeClose() {
        dismissAllowingStateLoss()
    }
}