package com.alcorp.moviecatalogue.di

import android.content.Context
import com.alcorp.moviecatalogue.data.source.AppRepository
import com.alcorp.moviecatalogue.data.source.local.LocalDataSource
import com.alcorp.moviecatalogue.data.source.local.room.AppDatabase
import com.alcorp.moviecatalogue.data.source.remote.RemoteDataSource
import com.alcorp.moviecatalogue.utils.ApiConfig
import com.alcorp.moviecatalogue.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context) : AppRepository {

        val database = AppDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig())
        val localDataSource = LocalDataSource.getInstance(database.appDao())
        val appExecutors = AppExecutors()

        return AppRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}