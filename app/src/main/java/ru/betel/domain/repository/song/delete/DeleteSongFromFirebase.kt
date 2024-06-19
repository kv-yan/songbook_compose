package ru.betel.domain.repository.song.delete

import ru.betel.domain.model.Song

interface DeleteSongFromFirebase {
    suspend fun deleteSong(song: Song): Boolean
}