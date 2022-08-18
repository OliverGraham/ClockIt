package com.olivergraham.clockit.feature_activity.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.olivergraham.clockit.feature_activity.presentation.activities.ActivityScreen
import com.olivergraham.clockit.feature_activity.presentation.add_edit_activity.AddEditActivityScreen
import com.olivergraham.clockit.feature_activity.presentation.utility.Screen
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
            route = Screen.AddEditActivityScreen.route +
                    "?activityId={activityId}&activityColor={activityColor}",
            arguments = getAddEditScreenArguments()
        ) { navBackStackEntry ->
            val color = navBackStackEntry.arguments?.getInt("activityColor") ?: -1
            AddEditActivityScreen(
                navController = navController,
                activityColor = color
            )
        }
    }
}

// TODO: deal with the string routes... yikes

private fun getAddEditScreenArguments(): List<NamedNavArgument> {
    return listOf(
        navArgument(
            name = "activityId"
        ) { ->
            type = NavType.IntType
            defaultValue = -1
        },
        navArgument(
            name = "activityColor"
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
