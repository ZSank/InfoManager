package com.app.infomanager.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.viewModel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	modifier: Modifier = Modifier, navigateToView: (Item) -> Unit, navigateToAdd: () -> Unit
) {
	val vm: SharedViewModel = hiltViewModel()
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
	var menuExpanded by remember { mutableStateOf(false) }
	
	val allItems by vm.allItems.collectAsState()
	Scaffold(
		floatingActionButton = {
			FloatingActionButton(navigateToAdd) {
				Icon(Icons.Default.Add, contentDescription = "Menu")
			}
		}, topBar = {
			TopAppBar(title = { }, scrollBehavior = scrollBehavior, actions = {
				IconButton(onClick = { menuExpanded = true }) {
					Icon(Icons.Default.MoreVert, contentDescription = "Menu")
				}
				DropdownMenu(
					expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
					DropdownMenuItem(text = { Text("About") }, onClick = {
						menuExpanded = false
					})
					DropdownMenuItem(text = { Text("Add random Task") }, onClick = {
						vm.addRandomItem()
						menuExpanded = false
					})
					DropdownMenuItem(text = { Text("Prepopulate DB") }, onClick = {
						vm.prepopulateDB()
						menuExpanded = false
					})
					DropdownMenuItem(text = { Text("Clear DB") }, onClick = {
						vm.deleteAll()
						menuExpanded = false
					})
				}
			})
		}, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
	) { innerPadding ->
		LazyColumn(
			modifier
				.padding(innerPadding)
				.padding(start = 10.dp, end = 10.dp)
		) {
			items(items = allItems) { item ->
				ItemUi(
					modifier.clickable { navigateToView(item) }, item
				)
			}
			item {
				Text(text = "End", modifier.padding(top = 10.dp, bottom = 10.dp))
			}
		}
	}
}

@Preview
@Composable
fun AddItemScreen(modifier: Modifier = Modifier, navigateUp: () -> Unit) {
	val vm: SharedViewModel = hiltViewModel()
	Scaffold { ip ->
		Column(
			modifier
				.padding(ip)
				.padding(start = 20.dp, end = 20.dp)
		) {
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = vm.name,
				onValueChange = { vm.name = it },
				label = { Text(text = "Name") },
				textStyle = MaterialTheme.typography.bodyMedium,
				singleLine = true
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = vm.category,
				onValueChange = { vm.category = it },
				label = { Text(text = "Category") },
				textStyle = MaterialTheme.typography.bodyMedium,
				singleLine = true
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = vm.barcode,
				onValueChange = {
					vm.barcode = it
				},
				label = { Text(text = "BarCode") },
				textStyle = MaterialTheme.typography.bodyMedium,
				singleLine = true,
				keyboardOptions = KeyboardOptions.Default.copy(
					keyboardType = KeyboardType.Number
				)
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = vm.description,
				onValueChange = {
					vm.description = it
				},
				label = { Text(text = "Description") },
				textStyle = MaterialTheme.typography.bodyMedium,
				singleLine = false
			)
			
			Button({
				vm.addCurrentItem()
				navigateUp()
			}) { Text("Add Item") }
		}
	}
}

@Composable
fun ViewItemScreen(modifier: Modifier = Modifier, item: Item, navigateUp: () -> Unit) {
	val vm: SharedViewModel = hiltViewModel()
	LaunchedEffect(item) {
		Log.d("mLog", "ViewItemScreen: $item")
	}
	val currentItem by vm.getItemById(item.uid).collectAsState(initial = null)
	LaunchedEffect(currentItem) {
		Log.d("mLog", "ViewItemScreen: $currentItem ")
	}
	Scaffold { ip ->
		Box(modifier.padding(ip)) {
			Button(
				{
					navigateUp()
					currentItem?.let { vm.delete(it) }
				}, modifier
					.align(Alignment.TopEnd)
					.padding(20.dp)
			) {
				Text("Delete")
			}
			Column {
				ItemUi(modifier, currentItem)
			}
		}
	}
}

@Composable
fun ItemUi(modifier: Modifier = Modifier, item: Item?) {
	Column(
		modifier
			.fillMaxWidth()
			.padding(top = 5.dp, bottom = 5.dp)
	) {
		item?.let {
			Text("Name: ${item.name}")
			if (item.category.isNotBlank()) Text("Category:${item.category}")
			if (item.barcode != 0L) Text("Barcode:${item.barcode}")
			Text("Qty:${item.qty}")
		}
	}
}