package com.onemb.myshoppinglistapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingListEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "itemName") val itemName: String,
    @ColumnInfo(name = "itemQuantity") val itemQuantity: String,
    @ColumnInfo(name = "itemEditedOn") val itemEditedOn: String,
    @ColumnInfo(name = "markCompleted") val markCompleted: Boolean
)