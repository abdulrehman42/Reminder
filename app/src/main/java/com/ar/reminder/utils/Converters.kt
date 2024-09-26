package com.ar.reminder.utils

import androidx.room.TypeConverter
import com.ar.reminder.model.ListResponseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()

    // Convert List<String> to a JSON string
    @TypeConverter
    @JvmStatic
    fun fromStringList(value: List<String>?): String? {
        return value?.let { gson.toJson(it) }
    }

    // Convert a JSON string to List<String>
    @TypeConverter
    @JvmStatic
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(it, listType)
        }
    }

    // Convert List<ListResponseModel.ResponseModelItem> to JSON string
    @TypeConverter
    @JvmStatic
    fun fromResponseModelItemList(value: List<ListResponseModel.ResponseModelItem>?): String? {
        return value?.let { gson.toJson(it) }
    }

    // Convert JSON string to List<ListResponseModel.ResponseModelItem>
    @TypeConverter
    @JvmStatic
    fun toResponseModelItemList(value: String?): List<ListResponseModel.ResponseModelItem>? {
        return value?.let {
            val listType = object : TypeToken<List<ListResponseModel.ResponseModelItem>>() {}.type
            gson.fromJson(it, listType)
        }
    }

    // Convert ListResponseModel.Schedule to JSON string
    @TypeConverter
    @JvmStatic
    fun fromSchedule(schedule: ListResponseModel.Schedule?): String? {
        return schedule?.let { gson.toJson(it) }
    }

    // Convert JSON string to ListResponseModel.Schedule
    @TypeConverter
    @JvmStatic
    fun toSchedule(scheduleString: String?): ListResponseModel.Schedule? {
        return scheduleString?.let {
            gson.fromJson(it, ListResponseModel.Schedule::class.java)
        }
    }

    // Convert ListResponseModel.LastSchedule to JSON string
    @TypeConverter
    @JvmStatic
    fun fromLastSchedule(schedule: ListResponseModel.LastSchedule?): String? {
        return schedule?.let { gson.toJson(it) }
    }

    // Convert JSON string to ListResponseModel.LastSchedule
    @TypeConverter
    @JvmStatic
    fun toLastSchedule(scheduleString: String?): ListResponseModel.LastSchedule? {
        return scheduleString?.let {
            gson.fromJson(it, ListResponseModel.LastSchedule::class.java)
        }
    }

    // Convert List<ListResponseModel.Notification> to JSON string
    @TypeConverter
    @JvmStatic
    fun fromNotificationList(value: List<ListResponseModel.Notification>?): String? {
        return value?.let { gson.toJson(it) }
    }

    // Convert JSON string to List<ListResponseModel.Notification>
    @TypeConverter
    @JvmStatic
    fun toNotificationList(value: String?): List<ListResponseModel.Notification>? {
        return value?.let {
            val listType = object : TypeToken<List<ListResponseModel.Notification>>() {}.type
            gson.fromJson(it, listType)
        }
    }

    // Convert List<ListResponseModel.ReminderNotificationsV2> to JSON string
    @TypeConverter
    @JvmStatic
    fun fromReminderNotificationsV2List(value: List<ListResponseModel.ReminderNotificationsV2>?): String? {
        return value?.let { gson.toJson(it) }
    }

    // Convert JSON string to List<ListResponseModel.ReminderNotificationsV2>
    @TypeConverter
    @JvmStatic
    fun toReminderNotificationsV2List(value: String?): List<ListResponseModel.ReminderNotificationsV2>? {
        return value?.let {
            val listType = object : TypeToken<List<ListResponseModel.ReminderNotificationsV2>>() {}.type
            gson.fromJson(it, listType)
        }
    }

    // Convert ListResponseModel.ScheduleV2 to JSON string
    @TypeConverter
    @JvmStatic
    fun fromReminderScheduleV2(schedule: ListResponseModel.ScheduleV2?): String? {
        return schedule?.let { gson.toJson(it) }
    }

    // Convert JSON string to ListResponseModel.ScheduleV2
    @TypeConverter
    @JvmStatic
    fun toReminderScheduleV2(scheduleString: String?): ListResponseModel.ScheduleV2? {
        return scheduleString?.let {
            gson.fromJson(it, ListResponseModel.ScheduleV2::class.java)
        }
    }
}