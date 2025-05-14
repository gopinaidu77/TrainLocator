package com.agn.trackmytrain.api

data class TrainLocationResponse(
    val status: Boolean,
    val message: String?,
    val data: Data?
) {
    data class Data(
        val current_station: String?,
        val expected_arrival: String?,
        val expected_departure: String?,
        val delay: String?,
        val last_updated: String?
    )
}
