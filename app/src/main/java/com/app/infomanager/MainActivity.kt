package com.app.infomanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.infomanager.data.models.Item
import com.app.infomanager.ui.theme.InfomanagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.pow
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		
		setContent {
			InfomanagerTheme {
				HomeScreen()
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	InfomanagerTheme {
		Greeting("Android")
	}
}

@Preview(showBackground = true)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
	Scaffold(
		floatingActionButton = {
			FloatingActionButton({}) {
			
			}
		}
	) { innerPadding ->
		LazyColumn(modifier.padding(innerPadding)) {
			item {
				Text(text = "First item")
			}
			
			items(15) { index ->
				ItemUi(
					modifier,
					Item(
						getRandomNumber(3),
						"Item$index",
						"cat$index",
						getRandomNumber(2),
						"",
						getRandomNumber(10).toLong()
					)
				)
			}
			item {
				Text(text = "Last item")
			}
		}
	}
}

@Composable
fun AddItem(modifier: Modifier = Modifier) {

}

@Composable
fun ViewItem(modifier: Modifier = Modifier) {

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
