package ecrans

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.monapp.MainViewModel
import com.example.monapp.R
import com.example.monapp.models.ActeurModel
import com.example.monapp.models.Movie
import com.example.monapp.models.SeriesModel

@Composable
fun Screen() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    Scaffold(
        topBar = {
            AppBar(viewModel = viewModel)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("films") { FilmsScreen(viewModel = viewModel) }
            composable("series") { SeriesScreen(viewModel = viewModel) }
            composable("actors") { ActorsScreen(viewModel = viewModel) }
        }
    }
}


// AppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(viewModel: MainViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    androidx.compose.material.TopAppBar(
        title = {
            Text(text = "Fav'app")
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Rechercher...") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(end = 8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent
                    )
                )
                IconButton(onClick = {
                    viewModel.setSearchQuery(searchQuery)
                    Log.d("Search", "Recherche pour : $searchQuery")
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }
        }
    )
}


@Composable
fun HomeScreen(navController: androidx.navigation.NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileSection()
        Spacer(modifier = Modifier.height(20.dp))
        ContactSection(context)

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { navController.navigate("films") }) {
            Text(text = "Démarrer")
        }
    }
}

@Composable
fun ProfileSection() {
    Image(
        painter = painterResource(id = R.drawable.maphoto),
        contentDescription = "Ma photo",
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
    )
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Yosr BEN JABEUR",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(8.dp)
    )

    Spacer(modifier = Modifier.height(5.dp))

    Text(
        text = "Étudiante développeuse passionnée par la création d'apps innovantes.",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun ContactSection(context: android.content.Context) {
    Text(
        text = "Contact : yosr.benjabeur@gmail.com",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(8.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "LinkedIn",
        style = MaterialTheme.typography.bodyLarge.copy(
            color = Color(0xFF0A66C2),
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier
            .clickable {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/yosr-jabeur"))
                context.startActivity(intent)
            }
            .padding(8.dp)
    )
}

// Films
@Composable
fun FilmsScreen(viewModel: MainViewModel) {
    val movies by viewModel.movies.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTrendingMovies()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie, viewModel = viewModel)
        }
    }

}

@Composable
fun MovieItem(movie: Movie, viewModel: MainViewModel) {
    val imageUrl = viewModel.getImageUrl(movie.poster_path)
    var isHovered by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable {  },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1
            )
            Text(
                text = "⭐ ${movie.vote_average} / 10",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFFD700),
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

//Series
@Composable
fun SeriesScreen(viewModel: MainViewModel) {
    val series by viewModel.series.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.lastSeries()
    }
    Log.d("SeriesScreen", "Series: ${series.size}")

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(series) { serie ->
            SeriesItem(serie)
        }
    }
}

@Composable
fun SeriesItem(serie: SeriesModel) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable {  },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = "https://image.tmdb.org/t/p/w300${serie.poster_path}"
        Card(
            modifier = Modifier
                .size(150.dp)
                .clip(MaterialTheme.shapes.medium)
                .shadow(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = serie.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = serie.name, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        Text(text = serie.first_air_date, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)


        Text(
            text = serie.genre_ids.joinToString(", "), //`
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}


//acteurs
@Composable
fun ActorsScreen(viewModel: MainViewModel) {
    val actors by viewModel.acteurs.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.lastActors()
    }
    Log.d("ActorsScreen", "Actors: ${actors.size}")

    if (actors.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(actors) { actor ->
                ActorItem(actor)
            }
        }
    } else {
        Text(
            text = "Aucun acteur trouvé",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActorItem(actor: ActeurModel) {
    val imageUrl = "https://image.tmdb.org/t/p/w300${actor.profile_path}"
    val placeholderImage = painterResource(id = R.drawable.placeholder)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {  },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = actor.name,
                error = placeholderImage,
                fallback = placeholderImage,
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = actor.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = actor.known_for_department,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}



@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavHostController) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.movie ), contentDescription = "Films") },
            label = { Text("Films") },
            selected = navController.currentDestination?.route == "films",
            onClick = {
                navController.navigate("films") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.tv), contentDescription = "Séries") },
            label = { Text("Séries") },
            selected = navController.currentDestination?.route == "series",
            onClick = {
                navController.navigate("series") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(painterResource(id = R.drawable.persons), contentDescription = "Acteurs") },
            label = { Text("Acteurs") },
            selected = navController.currentDestination?.route == "actors",
            onClick = {
                navController.navigate("actors") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
