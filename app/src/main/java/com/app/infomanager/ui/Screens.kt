package com.app.infomanager.ui

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.app.infomanager.R
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.viewModel.SharedViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

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

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BarcodeScannerScreen(
	modifier: Modifier = Modifier,
	navigateOnScanned: (String) -> Unit,
) {
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val cameraPermissionState = rememberPermissionState(
		android.Manifest.permission.CAMERA
	)
	
	Scaffold() { ip ->
		if (cameraPermissionState.status.isGranted) {
			AndroidView(
				factory = { ctx ->
					val previewView = PreviewView(ctx)
					val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
					
					cameraProviderFuture.addListener({
						val cameraProvider = cameraProviderFuture.get()
						val preview = androidx.camera.core.Preview.Builder().build().also {
							it.surfaceProvider = previewView.surfaceProvider
						}
						
						val barcodeScanner = BarcodeScanning.getClient()
						
						val imageAnalyzer = ImageAnalysis.Builder()
							.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
							.build()
							.also {
								it.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
									val mediaImage = imageProxy.image
									if (mediaImage != null) {
										val inputImage = InputImage.fromMediaImage(
											mediaImage, imageProxy.imageInfo.rotationDegrees
										)
										barcodeScanner.process(inputImage)
											.addOnSuccessListener { barcodes ->
												barcodes.firstOrNull()?.rawValue?.let { code ->
													Log.d("mLog", "BarcodeScannerScreen: $code")
													navigateOnScanned(code)
												}
											}.addOnCompleteListener {
												imageProxy.close()
											}
									} else {
										imageProxy.close()
									}
								}
							}
						
						val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
						
						cameraProvider.unbindAll()
						cameraProvider.bindToLifecycle(
							lifecycleOwner, cameraSelector, preview, imageAnalyzer
						)
					}, ContextCompat.getMainExecutor(ctx))
					
					previewView
				}, modifier = Modifier.fillMaxSize()
			)
		} else {
			Column(
				modifier
					.padding(ip)
					.padding(20.dp)
					.padding(top = 30.dp)
					.fillMaxSize(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
					"To use barcode scanner. Please grant the permission."
				} else {
					"Camera permission required for Barcode scanner"
				}
				Text(textToShow, modifier.padding(bottom = 50.dp))
				Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
					Text("Request permission")
				}
			}
		}
	}
	
}
