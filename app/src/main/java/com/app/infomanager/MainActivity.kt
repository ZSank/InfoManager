package com.app.infomanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.theme.InfomanagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.random.Random

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
data class ViewItem(val id: Int, val name: String)

@Composable
fun InfoManagerApp(modifier: Modifier = Modifier) {
	val navController = rememberNavController()
	NavHost(navController = navController, startDestination = Home) {
		composable<Home> {
			HomeScreen(
				navigateToView = { item -> navController.navigate(route = ViewItem(item.uid, item.name)) },
				navigateToAdd = { navController.navigate(route = AddItem) })
		}
		composable<ViewItem> { backStackEntry  ->
			val viewItem = backStackEntry.toRoute<ViewItem>()
			ViewItemScreen(modifier, Item(viewItem.id, viewItem.name, "", 0))
		}
		composable<AddItem> { AddItemScreen() }
	}
}

@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navigateToView: (Item) -> Unit,
	navigateToAdd: () -> Unit
) {
	Scaffold(
		floatingActionButton = {
			FloatingActionButton(navigateToAdd) {
			}
		}
	) { innerPadding ->
		LazyColumn(modifier.padding(innerPadding)) {
			item {
				Text(text = "First item")
			}
			
			items(15) { index ->
				val item = Item(
					getRandomNumber(3),
					"Item$index",
					"cat$index",
					getRandomNumber(2),
					"",
					getRandomNumber(10).toLong()
				)
				ItemUi(
					modifier.clickable { navigateToView(item) },
					item
				)
			}
			item {
				Text(text = "Last item")
			}
		}
	}
}

@Composable
fun AddItemScreen(modifier: Modifier = Modifier) {
	Text("AddItemScreen")
}

@Composable
fun ViewItemScreen(modifier: Modifier = Modifier, item: Item) {
	ItemUi(modifier.padding(top = 40.dp, bottom = 20.dp), item)
}

@Composable
fun ItemUi(modifier: Modifier = Modifier, item: Item) {
	Column(
		modifier
			.fillMaxWidth()
			.padding(top = 0.dp, bottom = 5.dp)
	) {
		Text("Id: ${item.uid}")
		Text("Name: ${item.name ?: ""}")
		if (!item.category.isNullOrBlank()) Text("Category:${item.category}")
		if (item.barcode != 0L) Text("Barcode:${item.barcode}")
	}
}


fun getRandomNumber(digits: Int = 4) =
	Random.nextInt(10.0.pow(digits - 1).toInt(), 10.0.pow(digits).toInt())
