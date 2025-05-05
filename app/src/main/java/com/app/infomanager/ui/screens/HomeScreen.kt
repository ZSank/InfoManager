package com.app.infomanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.infomanager.R
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.component.ItemUi
import com.app.infomanager.ui.viewModel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navigateToView: (Item) -> Unit,
	navigateToAdd: () -> Unit,
	navigateToScanner: () -> Unit,
	vm: SharedViewModel
) {
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
	var menuExpanded by remember { mutableStateOf(false) }
	
	val allItems by vm.allItems.collectAsState()
	Scaffold(
		floatingActionButton = {
			Column(modifier.padding(20.dp)) {
				FloatingActionButton(navigateToScanner) {
					Icon(
						painter = painterResource(id = R.drawable.barcode_scanner),
						contentDescription = "Scanner"
					)
				}
				Spacer(modifier.height(20.dp))
				FloatingActionButton(navigateToAdd) {
					Icon(Icons.Default.Add, contentDescription = "Menu")
				}
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
				.padding(start = 20.dp, end = 20.dp)
		) {
			itemsIndexed(allItems) { index, item ->
				ItemUi(
					modifier.clickable { navigateToView(item) }, item
				)
				if (index < allItems.lastIndex) {
					HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
				}
			}
			if (allItems.isEmpty()) item { Text("No Items...", color = Color.LightGray) }
		}
	}
}
