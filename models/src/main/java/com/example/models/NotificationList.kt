package com.example.models

class NotificationList<T>(
    var id: String,
    var title: String,
    var notifications: ArrayList<T>
)

data class Notification(
    val worker_id: Int,
    val worker_full_name: String,
    val worker_staff_number: String,
    val worker_position_title: String,

    val siz_id: Int,
    val siz_title: String,

    val checkup_date_start: String,
    val checkup_date_end: String,
    val flag: Boolean,
    val status_id: Int,

    val ppk_id: Int,
    val ppk_date_time: String,
    val ppk_status_id: Int,
    val checking_id: Int,
    val injunction_id: Int,

    val audit_id: Int,
    val audit_place_id: Int,
    val audit_place_title: String,
    val audit_date_time: String,

    val check_knowledge_date_time: String,

    val briefing_date_time: String,
)

