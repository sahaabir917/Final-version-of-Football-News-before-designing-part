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

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.Adapters.FootballAdapter
import com.example.android.marsrealestate.network.FootballApi
import com.example.android.marsrealestate.network.FootballList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class MarsApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    var totalpages: Int = 0
    var pageId : Int = 1
    val pagesize = 15

    val status: LiveData<String>
        get() = _status

    lateinit var adapter: FootballAdapter
    var totalpage : Int = 0
    var  i : Int =1

    private val _properties = MutableLiveData<FootballList>()
    val properties : LiveData<FootballList>
        get() = _properties


    init {
        getFootballProperties()
    }


    private fun getFootballProperties() {

       var  username : String = "D4CFDE0CDD141AA"
        var password : String = "AD743A46C3C"

        var base : String  = "$username:$password"

        var authHeader : String = "Basic " + Base64.encodeToString(base.toByteArray(),Base64.NO_WRAP)

        FootballApi.retrofitService.getdata(pagesize, pageId,authHeader).enqueue(object : retrofit2.Callback<FootballList> {
            override fun onFailure(call: Call<FootballList>, t: Throwable) {
                Log.d("data retrive failed", "failed to retrive")
                getFootballProperties()
            }

            override fun onResponse(call: Call<FootballList>, response: Response<FootballList>) {
                _properties.value = response.body()!!
                totalpages = response.body()!!.page.totalPages
            }
        })
    }


    fun retrofitcalling(pageid: Int) {
        var  username : String = "D4CFDE0CDD141AA"
        var password : String = "AD743A46C3C"

        var base : String  = "$username:$password"

        var authHeader : String = "Basic " + Base64.encodeToString(base.toByteArray(),Base64.NO_WRAP)

        FootballApi.retrofitService.getdata(pagesize, pageid,authHeader).enqueue(object : Callback<FootballList> {
            override fun onFailure(call: Call<FootballList>, t: Throwable) {
                Log.d("Abir", "Failed to retrive")
                pageId--
                Log.d("after fail to retrive data ", "pageid = $pageid")
            }

            override fun onResponse(call: Call<FootballList>, response: Response<FootballList>) {
                i++
                Log.d("Abir", "responsed" + i.toString())
                _properties.value = response.body()!!
                totalpages = response.body()!!.page.totalPages
            }
        })
    }
}