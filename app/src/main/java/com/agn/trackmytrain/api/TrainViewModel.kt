package com.agn.trackmytrain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agn.trackmytrain.api.RetrofitClient
import com.agn.trackmytrain.api.TrainLocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TrainViewModel : ViewModel() {

    fun getTrainLocation(
        trainNo: String,
        onSuccess: (TrainLocationResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                val response = RetrofitClient.apiService
                    .getTrainLocation(trainNo = trainNo, startDate = currentDate)
                    .execute()

                if (response.isSuccessful && response.body() != null) {
                    onSuccess(response.body()!!)
                } else {
                    onError(HttpException(response))
                }
            } catch (e: IOException) {
                onError(e)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
