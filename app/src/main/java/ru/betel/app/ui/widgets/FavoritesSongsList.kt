package ru.betel.app.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.betel.app.ui.items.song.FavoriteSongItemWithWords
import ru.betel.domain.model.Song
import ru.betel.domain.model.ui.SongbookTextSize

@Composable
fun FavoriteSongsList(
    songs: List<Song>,
    songbookTextSize: SongbookTextSize,
    onSongSelected: (Song) -> Unit,
    onRemoveFavSong: (Song) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(songs) { song ->
                FavoriteSongItemWithWords(
                    item = song,
                    textSize = songbookTextSize,
                    onItemClick = { onSongSelected(song) }) {
                    onRemoveFavSong(it)
                }
            }
        }
    }
}
