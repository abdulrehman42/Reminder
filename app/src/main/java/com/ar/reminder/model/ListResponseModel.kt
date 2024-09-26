package com.ar.reminder.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ar.reminder.utils.Converters

class ListResponseModel:ArrayList<ListResponseModel.ResponseModelItem>()
{
    @Entity(tableName = "response_model_item_table")
    @TypeConverters(Converters::class)
    data class ResponseModelItem(
        var __v: Int?=null,
        @PrimaryKey var _id: String = "",
        var clientId: String? = "",
        var createdAt: String? = "",
        var createdBy: String? = "",
        var ctaOrdering: Int?=null,
        var devices: List<String>?= emptyList(),
        var duration: Int?=null,
        var enableVacationMode: Boolean?=false,
        var entityType: String? = "",
        var folder: String? = "",
        var folderId: String? = "",
        var isScheduledByV2: Boolean?=false,
        var label: String? = "",
        var lastSchedule: LastSchedule?=null,
        var libraryType: String? = "",
        var limitRunPerDay: Boolean?=false,
        var limitRunPerHour: Boolean?=false,
        var name: String? = "",
        var narration: Boolean?=false,
        var notifications: List<Notification>?= emptyList(),
        var notifsV2SoundName: String? = "",
        var notifsV2SoundUrl: String? = "",
        var numberOfRunPerDay: Int?=null,
        var numberOfRunPerHour: Int?=null,
        var ordering: Int?=null,
        var reminderNotificationsV2: List<ReminderNotificationsV2>?= emptyList(),
        var schedule: Schedule?=null,
        var scheduleV2: ScheduleV2?=null,
        var tags: List<String>?= emptyList(),
        var type: String? = "",
        var updatedAt: String? = "",
        var visualAidUrl: String? = ""
    )

    data class Schedule(
        var Fri: Any?=null,
        var Mon: Any?=null,
        var Sat: Any?=null,
        var Sun: Any?=null,
        var Thu: Any?=null,
        var Tue: Any?=null,
        var Wed: Any?=null
    )

    data class ReminderNotificationsV2(
        var _id: String? = "",
        var minutesBefore: Int?=null,
        var notificationType: String? = ""
    )

    data class Notification(
        var _id: String? = "",
        var isNotificationSoundEnabled: Boolean?=false,
        var isPositiveReinfoSoundEnabled: Boolean?=false,
        var isReadReminderSoundEnabled: Boolean?=false,
        var minutesBefore: Int?=null,
        var notificationSoundUrl: String? = ""
    )

    data class LastSchedule(
        var Fri: Any?=null,
        var Mon: Any?=null,
        var Sat: Any?=null,
        var Sun: Any?=null,
        var Thu: Any?=null,
        var Tue: Any?=null,
        var Wed: Any?=null
    )

    data class DailyRepeatvarues(
        var Fri: List<String>?= emptyList(),
        var Mon: List<String>?= emptyList(),
        var Sat: List<String>?= emptyList(),
        var Sun: List<String>?= emptyList(),
        var Thu: List<String>?= emptyList(),
        var Tue: List<String>?= emptyList(),
        var Wed: List<String>?= emptyList()
    )

    data class ScheduleV2(
        var dailyRepeatvarues: DailyRepeatvarues,
        var timeType: String? = "",
        var timeValue: String? = "",
        var type: String? = "",
        var yearlyRepeatDatevarue: String? = ""
    )

}