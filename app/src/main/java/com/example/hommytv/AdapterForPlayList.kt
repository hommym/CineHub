package com.example.hommytv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.hommytv.roomdatabase.PlayListNameTable

//Purpose of this adapter to be used to show playlist in you fragment
class AdapterForPlayList(val context:Context,var data:List<PlayListNameTable>):RecyclerView.Adapter<AdapterForYouFragment.Holder>() {


    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        val image: ImageView = itemView.findViewById(R.id.img_you_frag)
        val title: TextView =itemView.findViewById(R.id.title_you_frag)
        val mediaType: TextView =itemView.findViewById(R.id.media_type_you_frag)
        val item: ConstraintLayout =itemView.findViewById(R.id.item_you_frag)
        val optionMenu: ImageView =itemView.findViewById(R.id.menu_item_you_frag)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterForYouFragment.Holder {
        val view:View= LayoutInflater.from(context).inflate(R.layout.content_you_fragment_design,parent,false)

        return AdapterForYouFragment.Holder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: AdapterForYouFragment.Holder, position: Int) {
        TODO("Not yet implemented")
    }
}