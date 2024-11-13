package ecrans

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.monapp.MainViewModel


@Composable
fun DestinationScreen() {
    val viewModel: MainViewModel = viewModel()

    val horrorCollections by viewModel.horrorCollections.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.searchHorrorCollections()
    }

    LazyColumn {
        items(horrorCollections) { collection ->
            Text(text = collection.name)
        }
    }

}

