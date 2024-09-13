package com.muco.disneyapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.muco.disneyapp.CustomNavType.DisneyCharacterType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Composable
fun DisneyApp() {
    val navController = rememberNavController()
    DisneyNavHost(
        navController = navController
    )
}

@Composable
fun DisneyNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = CharacterScreenRoute) {
        composable<CharacterScreenRoute> {
            DisneyCharactersScreen(onCharacterClick = { character ->
                navController.navigate(CharacterDetailScreenRoute(character))
            })
        }
        composable<CharacterDetailScreenRoute>(
            typeMap = mapOf(typeOf<CharacterModel>() to DisneyCharacterType)
        ) {
            val arguments = it.toRoute<CharacterDetailScreenRoute>()
            CharacterDetailScreen(character = arguments.character, onBackPressed = {
                navController.navigateUp()
            })
        }
    }
}

@Serializable
object CharacterScreenRoute

@Serializable
data class CharacterDetailScreenRoute(
    val character: CharacterModel
)