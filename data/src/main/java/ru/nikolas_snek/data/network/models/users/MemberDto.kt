package ru.nikolas_snek.data.network.models.users

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberDto(
    @Json(name = "email") val email: String,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "avatar_version") val avatarVersion: Int,
    @Json(name = "is_admin") val isAdmin: Boolean,
    @Json(name = "is_owner") val isOwner: Boolean,
    @Json(name = "is_guest") val isGuest: Boolean,
    @Json(name = "is_billing_admin") val isBillingAdmin: Boolean,
    @Json(name = "role") val role: Int,
    @Json(name = "is_bot") val isBot: Boolean,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "timezone") val timezone: String,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "date_joined") val dateJoined: String,
    @Json(name = "avatar_url") val avatarUrl: String?
)

@JsonClass(generateAdapter = true)
data class MembersResponseDto(
    @Json(name = "result") val result: String,
    @Json(name = "msg") val msg: String,
    @Json(name = "members") val members: List<MemberDto>
)