package com.example.android.marsrealestate.Adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.network.FootballData
import kotlinx.android.synthetic.main.grid_view_item.view.*
import kotlinx.android.synthetic.main.grid_view_item_2.view.*


class FootballAdapter() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
   lateinit var context: Context
    lateinit var footballdata: MutableList<FootballData>
    val contentview: Int = 0
    val Adviews: Int = 1
    val contentview1 = 2

    init {
        footballdata = mutableListOf<FootballData>()

    }

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(footballData: FootballData) {
            itemView.bodies.text = footballData.body.toString()
            itemView.id_number.text = footballData.id.toString()
            itemView.sharebutton.setOnClickListener{

            }
            var published_date : String = footballData.publishedAt.toString()
            itemView.published_at.text = "Published At : " + published_date.dropLast(16)

        }
    }

    class AdviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
//                what should be the context
        }
    }

    class Contentview1(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(footballData: FootballData){
        itemView.bodies2.text = footballData.body.toString()
            itemView.id_number2.text = footballData.id.toString()
            var publisheddate : String = footballData.publishedAt.toString()
            itemView.published_at2.text = publisheddate.dropLast(16)
            itemView.cardview1.setBackgroundResource(R.drawable.download)
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (position % 14 == 0) {
            Adviews
        } else {
            if(position % 2  == 0){
                contentview
            }
            else{
                contentview1
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return when(viewType) {
            contentview ->{
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item, parent, false)
             ContentViewHolder(view) }

            Adviews -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adbanner, parent, false)
                 AdviewHolder(view)
            }

            contentview1 -> {
                val view  = LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item_2,parent,false)
                Contentview1(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }


    }

    override fun getItemCount() = footballdata.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == contentview) {
            (holder as ContentViewHolder).bind(footballdata[position])
            holder.itemView.setOnClickListener { view: View ->
                val mArgs = Bundle()
                Log.d("argument will passed", footballdata[position].source)
                mArgs.putString("Key", footballdata[position].source.toString())
                view.findNavController()
                        .navigate(R.id.action_overviewFragment_to_detailsFragment, mArgs)
            }
        }

        if (getItemViewType(position) == Adviews) {
            (holder as AdviewHolder).bind()
        }

        if(getItemViewType(position) == contentview1){
            (holder as Contentview1).bind(footballdata[position])
            holder.itemView.setOnClickListener { view: View ->
                val mArgs = Bundle()
                Log.d("argument will passed", footballdata[position].source)
                mArgs.putString("Key", footballdata[position].source.toString())
                view.findNavController()
                        .navigate(R.id.action_overviewFragment_to_detailsFragment, mArgs)
            }
        }
    }

    public fun addFootballData(footballDataList: List<FootballData>) {
        this.footballdata.addAll(footballDataList)
    }

}

