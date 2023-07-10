package dev.kirstenbaker.gallery

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.kirstenbaker.gallery.ui.detail.ArtworkDetailScreen
import dev.kirstenbaker.gallery.ui.gallery.GalleryListScreen
import dev.kirstenbaker.gallery.viewmodel.GalleryViewModel

@Composable
fun Gallery(galleryViewModel: GalleryViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoute.GalleryList) {
        composable(route = NavRoute.GalleryList) {
            GalleryListScreen(
                galleryViewModel = galleryViewModel,
                onClickArtwork = { id ->
                    navController.navigate(
                        NavRoute.ArtworkDetailBase.plus(
                            id
                        ),
                    )
                }
            )
        }
        composable(route = NavRoute.ArtworkDetailBase.plus(NavRoute.ArtworkDetailUuid),
            arguments = listOf(
                navArgument(idNavInt) { type = NavType.IntType }
            )) { navBackStackEntry ->
            val navId = navBackStackEntry.arguments?.getInt(idNavInt)
            navId?.let {
                ArtworkDetailScreen() {
                    navController.popBackStack()
                }
            }
        }
    }
}

const val idNavInt = "id"

object NavRoute {
    const val GalleryList = "gallery"
    const val ArtworkDetailBase = "piece-detail"
    const val ArtworkDetailUuid = "{id}"
}