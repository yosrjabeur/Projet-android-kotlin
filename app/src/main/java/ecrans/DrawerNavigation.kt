import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.monapp.R
import ecrans.AppNavHost

@Composable
fun DrawerNavigation(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerItem(
                icon = { Icon(painterResource(id = R.drawable.movie), contentDescription = "Films") },
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
            NavigationDrawerItem(
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
            NavigationDrawerItem(
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
        },
        content = {
            AppNavHost(navController = navController)
        }
    )
}
