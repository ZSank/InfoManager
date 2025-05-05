package com.app.infomanager.data

import com.app.infomanager.data.models.Item
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemRepository @Inject constructor(private val dao: ItemDao) {
	val getAllItems: Flow<List<Item>> = dao.getAll()
	
	fun getItemById(id: Int): Flow<Item?> = dao.getItemById(id)
	
	suspend fun add(item: Item) {
		dao.add(item)
	}
	
	suspend fun remove(item: Item) {
		dao.delete(item)
	}
	
	suspend fun deleteAll() {
		dao.deleteAll()
	}
}