package com.example.cinema.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cinema.ui.data.model.MediaResponse
import com.example.cinema.ui.data.model.MediaResult
import com.example.cinema.ui.data.model.MediaType
import com.example.cinema.ui.screens.favorites.FavoriteViewModel
import com.example.cinema.ui.screens.favorites.RemoveFavoriteUiState
import com.example.cinema.ui.screens.home.MediaViewModel
import com.example.cinema.ui.theme.Primary
import com.example.cinema.ui.theme.Red
import com.example.cinema.ui.theme.Secondary
import com.example.cinema.ui.theme.White
import kotlinx.coroutines.launch


@Composable
fun LazyVerticalGridMedia(
    listAllMedia: List<MediaResult> = listOf(),
    navController: NavController,
    showCloseButtonCards:Boolean = false,
    nextPage:()->Int,
    stateScroll: LazyGridState = rememberLazyGridState()
    ){
    val mediaViewModel: MediaViewModel = viewModel<MediaViewModel>()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    LazyVerticalGrid(
        state = stateScroll,
        columns = if(screenWidth < 600) GridCells.Fixed(2) else GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Secondary),
    ){
        items(listAllMedia){
            ItemCardMovie(media = it, navController, showCloseButtonCards)
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            BottomBar(mediaViewModel, nextPage)
        }

    }


}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCardMovie(media:MediaResult, navController: NavController, showCloseButton:Boolean = false) {
    val scope = rememberCoroutineScope()
    val favoriteViewModel = viewModel<FavoriteViewModel>()

    if (showCloseButton)
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
                .offset(x = 10.dp, y = (-10).dp)
                .zIndex(1f),
            horizontalArrangement = Arrangement.End,

        ){
            IconButtonCinema(Icons.Filled.Clear, "Menu", White, Red, 26.dp){
                scope.launch {
                    favoriteViewModel.removeFavorite(media.id, media.mediaType)
                    navController.navigate("favorites")
               }
            }
        }

    Box(
        modifier = Modifier
            .aspectRatio(2f/3f)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                navController
                    .navigate(
                        if (media.mediaType == MediaType.MOVIE) "details/movies/${media.id}"
                        else "details/tvShow/${media.id}"
                    )
            }
        ) {

        val posterPath = media.posterPath

        //implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
        GlideImage(
            model = "https://image.tmdb.org/t/p/original$posterPath",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.ALL)
        }


    }
}

@Composable
fun BottomBar(moviesViewModel: MediaViewModel, nextPage:()->Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                moviesViewModel.getNextPageMedia(nextPage())

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = White
            )
        ) {
            Text(
                text = "Carregar",
                fontSize = 20.sp,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}