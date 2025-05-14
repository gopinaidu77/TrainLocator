package com.agn.trackmytrain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.agn.trackmytrain.api.TrainLocationResponse
import com.agn.trackmytrain.ui.theme.TrackMyTrainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackMyTrainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrainTrackerScreen()
                }
            }
        }
    }
}

@Composable
fun TrainTrackerScreen(viewModel: TrainViewModel = TrainViewModel()) {
    var trainNumber by remember { mutableStateOf(TextFieldValue("")) }
    var resultText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var trainLocation by remember { mutableStateOf<TrainLocationResponse.Data?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Train Tracker", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = trainNumber,
            onValueChange = { trainNumber = it },
            label = { Text("Enter Train Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val trainNo = trainNumber.text.trim()
                if (trainNo.isNotEmpty()) {
                    isLoading = true
                    resultText = ""
                    trainLocation = null

                    viewModel.getTrainLocation(
                        trainNo = trainNo,
                        onSuccess = { response ->
                            trainLocation = response.data
                            isLoading = false
                        },
                        onError = { error ->
                            resultText = "Error: ${error.localizedMessage}"
                            isLoading = false
                        }
                    )
                } else {
                    resultText = "Please enter a train number"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Loading..." else "Track Train")
        }

        trainLocation?.let { data ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📍 Current Station: ${data.current_station ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("🕒 Expected Arrival: ${data.expected_arrival ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("🕒 Expected Departure: ${data.expected_departure ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("⏳ Delay: ${data.delay ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("📅 Last Updated: ${data.last_updated ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (resultText.isNotBlank()) {
            Text(
                text = resultText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
