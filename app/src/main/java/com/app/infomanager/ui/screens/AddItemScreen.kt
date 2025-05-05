package com.app.infomanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.infomanager.R
import com.app.infomanager.ui.viewModel.SharedViewModel


@Preview
@Composable
fun AddItemScreen(
	modifier: Modifier = Modifier,
	navigateUp: () -> Unit,
	vm: SharedViewModel,
	navigateToScanner: () -> Unit
) {
	val keyboardController = LocalSoftwareKeyboardController.current
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
				value = vm.qty,
				onValueChange = {
					vm.qty = it
				},
				label = { Text(text = "Qty") },
				textStyle = MaterialTheme.typography.bodyMedium,
				singleLine = true,
				keyboardOptions = KeyboardOptions.Default.copy(
					keyboardType = KeyboardType.Number
				)
			)
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = vm.barcode,
				onValueChange = {
					vm.barcode = it
				},
				label = { Text(text = "Barcode") },
				textStyle = MaterialTheme.typography.bodyMedium,
				singleLine = true,
				keyboardOptions = KeyboardOptions.Default.copy(
					keyboardType = KeyboardType.Number
				),
				trailingIcon = {
					IconButton(onClick = { navigateToScanner() }) {
						Icon(
							painter = painterResource(id = R.drawable.barcode_scanner),
							contentDescription = "Scanner"
						)
					}
				}
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
				keyboardController?.hide()
				vm.addCurrentItem()
				vm.clearAllFields()
				navigateUp()
			}) { Text("Add Item") }
		}
	}
}