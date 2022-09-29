package com.olivergraham.clockit.feature_activity.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.olivergraham.clockit.feature_activity.presentation.activities.ActivityScreen
import com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.AddEditActivityScreen
import com.olivergraham.clockit.feature_activity.presentation.utility.Navigation
import com.olivergraham.clockit.feature_activity.presentation.utility.Screen
import com.olivergraham.clockit.feature_activity.presentation.utility.getColor
import com.olivergraham.clockit.feature_activity.presentation.utility.navigateToEditScreenRouteTemplate
import com.olivergraham.clockit.ui.theme.ClockItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClockItTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

/** Setup NavHost screens and arguments */
@Composable
private fun MainContent() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ActivityScreen.route
    ) { ->
        composable(route = Screen.ActivityScreen.route) { _ ->
            ActivityScreen(navController = navController)
        }
        composable(
            route = navController.navigateToEditScreenRouteTemplate(),
            arguments = getAddEditScreenArguments()
        ) { navBackStackEntry ->
            AddEditActivityScreen(
                navController = navController,
                activityColor = navBackStackEntry.getColor()
            )
        }
    }
}

/** Named arguments boilerplate */
private fun getAddEditScreenArguments(): List<NamedNavArgument> {
    return listOf(
        navArgument(
            name = Navigation.getActivityIdKey()
        ) { ->
            type = NavType.IntType
            defaultValue = -1
        },
        navArgument(
            name = Navigation.getActivityColorKey()
        ) { ->
            type = NavType.IntType
            defaultValue = -1
        }
    )
}


/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ClockItTheme {
        Greeting("Android")
    }
}*/
