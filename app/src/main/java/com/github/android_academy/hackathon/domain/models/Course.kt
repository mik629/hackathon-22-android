package com.github.android_academy.hackathon.domain.models

data class Course(
    val title: String,
    val shortDescription: String? = null,
    val fullDescription: String? = null,
    val imgUrl: String? = null,
    val tags: List<String>
)