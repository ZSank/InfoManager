package com.app.infomanager.ui.screens

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


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
