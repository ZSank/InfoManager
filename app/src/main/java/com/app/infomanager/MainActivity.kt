package com.app.infomanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.AddItemScreen
import com.app.infomanager.ui.BarcodeScannerScreen
import com.app.infomanager.ui.HomeScreen
import com.app.infomanager.ui.ViewItemScreen
import com.app.infomanager.ui.theme.InfomanagerTheme
import com.app.infomanager.ui.viewModel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		
		setContent {
			InfomanagerTheme {
				InfoManagerApp()
			}
		}
	}
}

@Serializable
object Home

@Serializable
object AddItem

@Serializable
data class ViewItem(val uid: Int)

@Serializable
data class BarcodeComp(val isNew: Boolean)

@Composable
fun InfoManagerApp(modifier: Modifier = Modifier) {
	val vm: SharedViewModel = hiltViewModel()
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = Home) {
		composable<Home> {
			HomeScreen(
				navigateToView = { item ->
					navController.navigate(
						route = ViewItem(
							item.uid
						)
					)
				},
				navigateToAdd = { navController.navigate(route = AddItem) },
				vm = vm,
				navigateToScanner = { navController.navigate(route = BarcodeComp(true)) })
		}
		composable<ViewItem> { backStackEntry ->
			val viewItem = backStackEntry.toRoute<ViewItem>()
			ViewItemScreen(modifier, Item(uid = viewItem.uid), navigateUp = {
				navController.navigateUp()
			}, vm)
		}
		composable<AddItem> {
			AddItemScreen(
				modifier, navigateUp = { navController.navigateUp() }, vm,
				navigateToScanner = { navController.navigate(route = BarcodeComp(false)) })
		}
		composable<BarcodeComp> { entry ->
			val isNew = entry.toRoute<BarcodeComp>().isNew
			BarcodeScannerScreen(navigateOnScanned = { code ->
				vm.barcode = code
				navController.popBackStack()
				if (isNew) navController.navigate(route = AddItem)
			})
		}
	}
}