package com.weekend.picturepreview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weekend.picturepreview.view.PhotoPreviewDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val mMainPicList= arrayListOf<String>().apply {
        add("https://img0.baidu.com/it/u=616172321,1682314558&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201912%2F13%2F20191213144139_MZ8tv.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1691568321&t=8d5c2eb7967c94f1122cf70cee772d06")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2Fb30e5fd4-cd9c-46f7-83d4-bd2e25af261d%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1691566158&t=5fee2e054104d0e952cb39c5b3e35be0")
        add("https://i1.hdslb.com/bfs/archive/a9a76afead6c75780d8fb9a095473d51afbe9eb6.jpg")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_recyclerView?.apply {
            adapter = MainRvAdapter(this@MainActivity) { mainRvHolder: MainRvHolder, i: Int, list: ArrayList<String> ->
                PhotoPreviewDialog.newInstance(list,i).show(supportFragmentManager,PhotoPreviewDialog.TAG)
            }
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


    inner class MainRvAdapter(private val context: Context, private val onClick: (MainRvHolder, Int, ArrayList<String>) -> Unit) : RecyclerView.Adapter<MainRvHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRvHolder {
            return MainRvHolder(LayoutInflater.from(context).inflate(R.layout.activity_main_apater_item,parent,false))
        }

        override fun onBindViewHolder(holder: MainRvHolder, position: Int) {
            holder.image?.tag = position
            Glide.with(holder!!.image!!).load(mMainPicList[position]).into(holder!!.image!!)
            holder.image?.setOnClickListener {
                onClick.invoke(holder,position,mMainPicList)
            }
        }

        override fun getItemCount(): Int {
            return mMainPicList.size
        }
    }

    inner class MainRvHolder(view: View):RecyclerView.ViewHolder(view){
        var image: AppCompatImageView?=null
        init {
            image=view.findViewById(R.id.item_img)
        }
    }
}