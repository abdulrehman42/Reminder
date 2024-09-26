package com.ar.reminder.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.ar.reminder.model.ListResponseModel
import com.ar.reminder.utils.Converters

@Dao
@TypeConverters(Converters::class)
interface ListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(listResponseModel: List<ListResponseModel.ResponseModelItem>)

    @Query("SELECT * FROM response_model_item_table WHERE _id = :id")
    fun getListResponseById(id: String): ListResponseModel.ResponseModelItem
}