package com.app.infomanager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.AddItemScreen
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

@Composable
fun InfoManagerApp(modifier: Modifier = Modifier) {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = Home) {
		composable<Home> {
			HomeScreen(navigateToView = { item ->
				navController.navigate(
					route = ViewItem(
						item.uid
					)
				)
			}, navigateToAdd = { navController.navigate(route = AddItem) })
		}
		composable<ViewItem> { backStackEntry ->
			val viewItem = backStackEntry.toRoute<ViewItem>()
			ViewItemScreen(modifier, Item(uid = viewItem.uid), navigateUp = {
				navController.navigateUp()
			})
		}
		composable<AddItem> { AddItemScreen(modifier, navigateUp = { navController.navigateUp() }) }
	}
}