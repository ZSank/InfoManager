package com.app.infomanager.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.infomanager.data.ItemRepository
import com.app.infomanager.data.models.Item
import com.app.infomanager.util.getRandomNumber
import com.app.infomanager.util.getRandomWord
import com.app.infomanager.util.toIntOrDef
import com.app.infomanager.util.toLongOrDef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
	private val repository: ItemRepository,
) : ViewModel() {
	init {
		getAllTasks()
	}
	
	val emptyItem = Item("", "")
	var id by mutableIntStateOf(0)
	var currentItem by mutableStateOf(emptyItem)
	var name by mutableStateOf("")
	var category by mutableStateOf("")
	var barcode by mutableStateOf("0")
	var qty by mutableStateOf("0")
	var description by mutableStateOf("")
	val allItems = MutableStateFlow<List<Item>>(emptyList())
	
	private fun getAllTasks() {
		try {
			viewModelScope.launch {
				repository.getAllItems.collect {
					allItems.value = it
				}
			}
		} catch (e: Exception) {
			allItems.value = emptyList()
		}
	}
	
	fun getItemById(id: Int): Flow<Item?> {
		return repository.getItemById(id)
	}
	
	fun add(item: Item) {
		viewModelScope.launch(Dispatchers.IO) {
			repository.add(item)
		}
	}
	
	fun delete(item: Item) {
		viewModelScope.launch {
			repository.remove(item)
		}
	}
	
	fun deleteCurrentItem() {
		viewModelScope.launch {
			repository.remove(currentItem)
		}
	}
	
	fun addCurrentItem() {
		
		add(
			Item(
				name,
				category,
				qty.toIntOrDef(),
				description,
				barcode.toLongOrDef()
			).also { log("$it") }
		)
	}
	
	fun prepopulateDB() {
		repeat(15) {
			add(getRandomItem())
		}
	}
	
	fun deleteAll() {
		viewModelScope.launch {
			repository.deleteAll()
		}
	}
	
	fun addRandomItem() {
		add(getRandomItem())
	}
	
	fun getRandomItem() = Item(
		getRandomWord(6),
		"random",
		getRandomNumber(2),
		getRandomWord(5) + getRandomWord(5),
		getRandomNumber(10).toLong()
	)
	
	fun log(text: String = "", identifier: String = "") {
		Log.d("Mlog", "$text: $identifier")
	}
}