package com.alcorp.moviecatalogue.data.source.local.entity

import androidx.room.Embedded

data class MovieItem (
        @Embedded
        var mMovie: MovieEntity
)