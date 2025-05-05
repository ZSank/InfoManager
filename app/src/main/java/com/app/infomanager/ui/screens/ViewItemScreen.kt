package com.app.infomanager.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.component.ItemUi
import com.app.infomanager.ui.viewModel.SharedViewModel


@Composable
fun ViewItemScreen(
	modifier: Modifier = Modifier,
	item: Item,
	navigateUp: () -> Unit,
	vm: SharedViewModel
) {
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