package com.app.infomanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
	@ColumnInfo(name = "name") val name: String = "",
	@ColumnInfo(name = "category") val category: String = "",
	@ColumnInfo(name = "qty") val qty: Int = 0,
	@ColumnInfo(name = "desc") val des: String = "",
	@ColumnInfo(name = "barcode") val barcode: Long = 0,
	@PrimaryKey(autoGenerate = true) val uid: Int = 0
)