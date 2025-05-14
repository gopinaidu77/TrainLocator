package com.agn.trackmytrain.api

data class TrainStatusResponse(
    val data: TrainData?,
    val message: String?,
    val status: Boolean
)

data class TrainData(
    val train_name: String?,
    val train_number: String?,
    val from_station: String?,
    val to_station: String?,
    val departure_time: String?,
    val arrival_time: String?,
    val running_days: List<String>?
)
