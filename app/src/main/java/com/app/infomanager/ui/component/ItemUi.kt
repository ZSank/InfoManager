package com.app.infomanager.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.infomanager.data.models.Item


@Composable
fun ItemUi(modifier: Modifier = Modifier, item: Item?) {
	Column(
		modifier
			.fillMaxWidth()
			.padding(top = 10.dp, bottom = 10.dp)
	) {
		item?.let {
			Text("Name: ${item.name}")
			if (item.category.isNotBlank()) Text("Category:${item.category}")
			if (item.barcode != 0L) Text("Barcode:${item.barcode}")
			Text("Qty:${item.qty}")
		}
	}
}