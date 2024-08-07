package ru.betel.app.ui.screens.splash


import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.StyledPlayerView
import ru.betel.app.R


import ru.betel.domain.model.ui.AppTheme

@Composable
fun SplashScreen(uri: Uri) {
    Surface(modifier = Modifier.fillMaxSize()) {
        VideoSplashScreen(uri)
    }
}


@Composable
fun VideoSplashScreen(videoUri: Uri) {
    val context = LocalContext.current
    val exoPlayer = remember { context.buildExoPlayer(videoUri) }

    DisposableEffect(
        AndroidView(
            factory = { it.buildPlayerView(exoPlayer) }, modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White
                )
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}

private fun Context.buildExoPlayer(uri: Uri) = ExoPlayer.Builder(this).build().apply {
    setMediaItem(MediaItem.fromUri(uri))
    this.isDeviceMuted = true
    repeatMode = Player.REPEAT_MODE_ALL
    playWhenReady = true
    prepare()
}

private fun Context.buildPlayerView(exoPlayer: ExoPlayer) = StyledPlayerView(this).apply {
    player = exoPlayer
    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    useController = false
    resizeMode = RESIZE_MODE_ZOOM
}


@Composable
fun SplashScreen(appTheme : AppTheme) {
    val offsetY = remember { Animatable((-200f)) }

    LaunchedEffect(Unit) {
        offsetY.animateTo(
            targetValue = 60f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        offsetY.animateTo(
            targetValue = (-0f),
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
//        offsetY.animateTo(
//            targetValue = 0f,
//            animationSpec = tween(durationMillis = 950, easing = FastOutSlowInEasing)
//        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(appTheme.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = null,
            tint = appTheme.primaryTextColor,
            modifier = Modifier
                .size(225.dp)
                .offset(y = offsetY.value.dp)
        )
    }
}
