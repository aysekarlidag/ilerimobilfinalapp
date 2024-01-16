package com.example.ilerimobilfinalapp.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private val appContext = getApplication<Application>().applicationContext

    val liveData = MutableLiveData<List<MovieEntity>>()

    fun getList() {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                if (isOnline()) {
                    val data = getApiData()
                    saveData(data)
                    data
                } else {
                    getData()
                }
            }

            liveData.value = result
        }
    }

    suspend fun getApiData(): List<MovieEntity> {
        val apiBuilder = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/skydoves/aa3bbbf495b0fa91db8a9e89f34e4873/raw/a1a13d37027e8920412da5f00f6a89c5a3dbfb9a/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = apiBuilder.create(AppApi::class.java)
        val result = api.getList()
        return result.body() ?: listOf()
    }

    fun saveData(data: List<MovieEntity>) {
        val builder = Room
            .databaseBuilder(appContext, Database::class.java, "HalicMovieDatabase")
            .fallbackToDestructiveMigration()
            .build()

        val dao = builder.dao()

        dao.insertAll(data)
    }

    fun getData(): List<MovieEntity> {
        val builder = Room
            .databaseBuilder(appContext, Database::class.java, "HalicMovieDatabase")
            .fallbackToDestructiveMigration()
            .build()

        val dao = builder.dao()

        return dao.getAll() ?: listOf()
    }

    fun isOnline(): Boolean {
        val connectivityManager = appContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        if (network == null) return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network)
        if (activeNetwork == null) return false

        if (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        ) {
            return true
        }

        return false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}