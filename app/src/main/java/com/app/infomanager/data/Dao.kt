package com.app.infomanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.infomanager.data.models.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
	@Query("SELECT * FROM item")
	fun getAll(): Flow<List<Item>>
	
	@Query("SELECT * FROM item ORDER BY uid ASC")
	fun getAllTasks(): Flow<List<Item>>
	
	@Query("SELECT * FROM item WHERE uid = :uid")
	fun getItemById(uid: Int): Flow<Item?>
	
	@Query("SELECT * FROM item WHERE uid IN (:itemIds)")
	fun loadAllByIds(itemIds: IntArray): List<Item>
	
	@Query(
		"SELECT * FROM item WHERE name LIKE :first AND " +
				"qty LIKE :last LIMIT 1"
	)
	fun findByName(first: String, last: String): Item
	
	@Insert
	fun addAll(vararg items: Item)
	
	@Insert
	suspend fun add(item: Item)
	
	@Delete
	suspend fun delete(item: Item)
	
	@Query("DELETE FROM Item")
	suspend fun deleteAll()
}