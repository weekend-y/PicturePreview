package com.weekend.picturepreview.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.weekend.picturepreview.R

class PreviewPictureView<E> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewPager by lazy { this.findViewById<ViewPager2>(R.id.view_pager_content) }
    private var mPagerAdapter :PagerAdapter<E>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.picture_preview_view_layout, this, true)
    }

    fun start(index:Int,data:ArrayList<E>,mAlphaCallback:AlphaCallback){
        mPagerAdapter=PagerAdapter(
            context, data
        ).apply {
            onBind { holder, position, data ->
                holder.imageView?.apply {
                    setAlphaCallback(mAlphaCallback)
                    if (position==index){
                        ViewCompat.setTransitionName(holder.imageView!!, "CONTENT")
                        ViewCompat.setTransitionName(viewPager, "-1")
                    }else{
                        ViewCompat.setTransitionName(holder.imageView!!, "index${position}")
                    }
                    Glide.with(context).load(data).into(holder.imageView!!)
                }
            }
        }

        viewPager.setCurrentItem(index?:0, false)
        viewPager.apply {
            adapter = mPagerAdapter
        }
        viewPager.setCurrentItem(index?:0, false)
    }
}