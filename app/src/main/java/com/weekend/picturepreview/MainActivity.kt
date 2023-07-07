package com.weekend.picturepreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.weekend.picturepreview.view.PhotoPreviewDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val testList= arrayListOf<String>().apply {
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn1-q.mafengwo.net%2Fs6%2FM00%2FFC%2FCC%2FwKgB4lNzI2yAK4tdAAELj6RBVtE37.jpeg%3FimageMogr2%252Fthumbnail%252F%21310x207r%252Fgravity%252FCenter%252Fcrop%252F%21310x207%252Fquality%252F90&refer=http%3A%2F%2Fn1-q.mafengwo.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627524301&t=b2b39785c50fd1130a19c7df69aee6d9")
        add("https://img1.baidu.com/it/u=1800733240,778446138&fm=224&fmt=auto&gp=0.jpg")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        testText?.setOnClickListener {
            PhotoPreviewDialog.newInstance(testList,0).show(supportFragmentManager,PhotoPreviewDialog.TAG)
        }


    }






}