package com.example.statisticmanager.common

import androidx.room.Entity

data class ModelState (
    val nameState: String,
    var count: Int,
    var team_1_count: Int,
    var team_2_count: Int,
)
