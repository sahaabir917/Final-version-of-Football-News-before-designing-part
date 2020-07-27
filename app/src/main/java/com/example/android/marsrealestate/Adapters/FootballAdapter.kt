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
import com.example.android.marsrealestate.network.FootballList
import kotlinx.android.synthetic.main.grid_view_item.view.*

class FootballAdapter() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    lateinit var footballdata: MutableList<FootballData>
    val contentview: Int = 0
    val Adviews: Int = 1

    init {
        footballdata = mutableListOf<FootballData>()
    }

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(footballData: FootballData) {
            itemView.bodies.text = footballData.body.toString()
            itemView.id_number.text = footballData.id.toString()
            itemView.published_at.text = footballData.publishedAt.toString()
        }
    }

    class AdviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
//                what should be the context
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (position % 30 == 0) {
            Adviews
        } else {
            contentview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == contentview) {
            val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.grid_view_item, parent, false)
            return ContentViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adbanner, parent, false)
            return AdviewHolder(view)
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
    }

    public fun addFootballData(footballDataList: List<FootballData>) {
        this.footballdata.addAll(footballDataList)
    }
}

