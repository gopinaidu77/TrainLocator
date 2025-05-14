package com.agn.trackmytrain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agn.trackmytrain.api.ApiClient
import com.agn.trackmytrain.api.TrainLocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TrainViewModel : ViewModel() {

    private val _trainLocation = MutableLiveData<TrainLocationResponse.Data?>()
    val trainLocation: LiveData<TrainLocationResponse.Data?> = _trainLocation

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Get current date in the format YYYYMMDD
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Fetch train location
    fun getTrainLocation(trainNo: String) {
        _isLoading.value = true
        _errorMessage.value = null  // Clear previous errors
        val startDate = getCurrentDate()  // Get today's date

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getTrainLocation(trainNo, startDate).execute()
                }

                if (response.isSuccessful && response.body() != null) {
                    _trainLocation.value = response.body()!!.data
                } else {
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Set custom error message
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}
