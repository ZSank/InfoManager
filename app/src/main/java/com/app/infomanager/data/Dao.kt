package com.app.infomanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.infomanager.data.models.Item

@Dao
interface ItemDao {
	@Query("SELECT * FROM item")
	fun getAll(): List<Item>
	
	@Query("SELECT * FROM item WHERE uid IN (:itemIds)")
	fun loadAllByIds(itemIds: IntArray): List<Item>
	
	@Query(
		"SELECT * FROM item WHERE name LIKE :first AND " +
				"qty LIKE :last LIMIT 1"
	)
	fun findByName(first: String, last: String): Item
	
	@Insert
	fun insertAll(vararg items: Item)
	
	@Delete
	fun delete(item: Item)
}