package com.agn.trackmytrain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.agn.trackmytrain.ui.theme.TrackMyTrainTheme
import com.agn.trackmytrain.viewmodel.TrainViewModel
import androidx.compose.runtime.livedata.observeAsState  // **Import this**

class MainActivity : ComponentActivity() {

    // Use lifecycle-aware ViewModel
    private val viewModel: TrainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackMyTrainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrainTrackerScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun TrainTrackerScreen(viewModel: TrainViewModel) {
    var trainNumber by remember { mutableStateOf(TextFieldValue("")) }

    // Observing LiveData from ViewModel using observeAsState
    val isLoading by viewModel.isLoading.observeAsState(false)
    val trainLocation by viewModel.trainLocation.observeAsState(null)
    val errorMessage by viewModel.errorMessage.observeAsState(null)

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

        // Track Train Button
        Button(
            onClick = {
                val trainNo = trainNumber.text.trim()
                if (trainNo.isNotEmpty()) {
                    viewModel.getTrainLocation(trainNo)
                } else {
                    viewModel.setErrorMessage("Please enter a train number")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading // Use the LiveData for button enabling
        ) {
            Text(if (isLoading) "Loading..." else "Track Train")
        }

        // Show Train Location Details
        trainLocation?.let { data ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📍 Current Station: ${data.current_station ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("🕒 Expected Arrival: ${data.expected_arrival ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("🕒 Expected Departure: ${data.expected_departure ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("⏳ Delay: ${data.delay ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                Text("📅 Last Updated: ${data.last_updated ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // Show Error Message
        errorMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
