package com.alcorp.moviecatalogue.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alcorp.moviecatalogue.data.source.remote.response.*
import com.alcorp.moviecatalogue.utils.ApiConfig
import com.alcorp.moviecatalogue.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val apiConfig: ApiConfig) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(helper: ApiConfig): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(helper)
            }
    }

    fun getListMovie(): LiveData<ApiResponse<MovieResponse>> {
        val resultMovie = MutableLiveData<ApiResponse<MovieResponse>>()


        val call: Call<MovieResponse> = apiConfig.getApiService().loadListMovie()
        call.enqueue(object : Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                EspressoIdlingResource.increment()
                resultMovie.value = ApiResponse.success(response.body()!!)
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("getListMovie", "onFailure: ${t.message.toString()}")
            }
        })
        return resultMovie
    }

    fun getMovie(id: String): LiveData<ApiResponse<MovieResponse>> {
        val resultMovie = MutableLiveData<ApiResponse<MovieResponse>>()

        val call: Call<MovieResponse> = apiConfig.getApiService().loadMovie(id)
        call.enqueue(object : Callback<MovieResponse>{
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful){
                    EspressoIdlingResource.increment()
                    resultMovie.value = ApiResponse.success(response.body()!!)
                    EspressoIdlingResource.decrement()
                } else {
                    Log.e("getMovie", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("getMovie", "onFailure: ${t.message.toString()}")
            }
        })
        return resultMovie
    }

    fun getListTv(): LiveData<ApiResponse<TvResponse>> {
        val resultTv = MutableLiveData<ApiResponse<TvResponse>>()

        val call: Call<TvResponse> = apiConfig.getApiService().loadListTv()
        call.enqueue(object : Callback<TvResponse>{
            override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                EspressoIdlingResource.increment()
                resultTv.value = ApiResponse.success(response.body()!!)
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.e("getListTv", "onFailure: ${t.message.toString()}")
            }
        })
        return resultTv
    }

    fun getTv(id: String): LiveData<ApiResponse<TvResponse>> {
        val resultTv = MutableLiveData<ApiResponse<TvResponse>>()

        val call: Call<TvResponse> = apiConfig.getApiService().loadTv(id)
        call.enqueue(object : Callback<TvResponse>{
            override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                if (response.isSuccessful){
                    EspressoIdlingResource.increment()
                    resultTv.value = ApiResponse.success(response.body()!!)
                    EspressoIdlingResource.decrement()
                } else {
                    Log.e("getTv", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Log.e("getTv", "onFailure: ${t.message.toString()}")
            }
        })
        return resultTv
    }
}