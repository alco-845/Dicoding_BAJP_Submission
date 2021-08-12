package com.alcorp.moviecatalogue.data.source.local.entity

import androidx.room.Embedded

data class TvShowItem (
    @Embedded
    var mTv: TvShowEntity
)