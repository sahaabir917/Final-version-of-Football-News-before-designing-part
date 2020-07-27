/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marsrealestate.Adapters.FootballAdapter
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.FragmentOverviewBinding
import com.example.android.marsrealestate.network.FootballList
import kotlinx.android.synthetic.main.fragment_overview.*


/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }


    lateinit var footballist : FootballList
    var isScrolling = false
    var currentItem: Int = 0
    var totalItem: Int = 0
    var scrolloutItems: Int = 0
    var pageid = 1
    val pagesize = 50
    lateinit var adapter: FootballAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var totalRecords: Int = 0
    var totalpages : Int = 0
    /**
     *
     *
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        adapter = FootballAdapter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.setHasFixedSize(true)

        viewModel.properties.observe(this, Observer {Footballist->
            Showalldata(Footballist)
            totalpages = Footballist.page.totalPages
            d("the total number of pages is", Footballist.page.totalPages.toString())
        })


        layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.layoutManager = layoutManager


        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem = layoutManager.childCount
                totalItem = layoutManager.itemCount
                Log.d("current item is ", currentItem.toString())
                Log.d("total items", totalItem.toString())
                scrolloutItems =
                        (recyclerview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (isScrolling && ((currentItem + scrolloutItems) == totalItem)) {
                    isScrolling = false
                    fetchagain(totalRecords)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })

        setHasOptionsMenu(false)
        return binding.root
    }

    private fun Showalldata(footballist: FootballList) {
        adapter.addFootballData(footballist.data)
        d("total adapter is - ", adapter.footballdata.size.toString())
        adapter.notifyDataSetChanged()
    }

    private fun fetchagain(totalrecords: Int) {

        Handler().postDelayed(Runnable {
            kotlin.run {
                pageid++
                if (totalpages >= pageid) {
                    viewModel.retrofitcalling(pageid)
                } else {
                    Toast.makeText(context, "No more News", Toast.LENGTH_SHORT).show()
                }
            }
        }, 5000)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}