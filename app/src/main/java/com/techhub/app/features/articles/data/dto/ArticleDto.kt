package com.techhub.app.features.articles.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String? = null,
    @SerialName("url") val url: String,
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("readable_publish_date") val readablePublishDate: String? = null,
    @SerialName("reading_time_minutes") val readingTimeMinutes: Int? = 1,
    @SerialName("tag_list") val tagList: List<String>? = emptyList(),
    @SerialName("user") val user: UserDto? = null
)

@Serializable
data class UserDto(
    @SerialName("name") val name: String? = "Dev Community",
    @SerialName("profile_image") val profileImage: String? = null
)
