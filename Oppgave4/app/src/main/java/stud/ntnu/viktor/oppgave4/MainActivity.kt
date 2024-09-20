package stud.ntnu.viktor.oppgave4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.sp
import stud.ntnu.viktor.oppgave4.ui.theme.Oppgave4Theme

data class Item(val title: String, val description: String, val imageRes: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Oppgave4Theme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val items = listOf(
        Item("Harry potter", "Harry potter and co is a cool film that all data engeneers should see. It features a young and cool boy that lives in england and likes to wave his wand", R.drawable.harrypotter),
        Item("Dark knight", "Batman is always cool, and I must say that he is the best super hero. Is is not too OP and has a cool background", R.drawable.batman),
        Item("Innsiden ut 2", "Denne filmen har preget mitt liv. Jeg så den i løpet av sommeren og har aldri blitt så traumatisert :D", R.drawable.innsiden)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    val currentItem = items[selectedIndex]

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fragmenter - øving") },
                actions = {
                    TextButton(onClick = {
                        selectedIndex = if (selectedIndex > 0) selectedIndex - 1 else items.lastIndex
                    }) {
                        Text("Forrige")
                    }
                    TextButton(onClick = {
                        selectedIndex = if (selectedIndex < items.lastIndex) selectedIndex + 1 else 0
                    }) {
                        Text("Neste")
                    }
                },
                modifier = Modifier.background(Color.Magenta)
            )
        },
        content = { paddingValues ->
            if (isPortrait) {
                Column(modifier = Modifier.padding(paddingValues)) {
                    ItemList(items = items, selectedIndex = selectedIndex) { index ->
                        selectedIndex = index
                    }
                    ItemDetail(item = currentItem)
                }
            } else {
                Row(modifier = Modifier.padding(paddingValues)) {
                    Box(modifier = Modifier.width(200.dp)) {
                        ItemList(items = items, selectedIndex = selectedIndex) { index ->
                            selectedIndex = index
                        }
                    }
                    ItemDetail(item = currentItem)
                }
            }
        }
    )
}

@Composable
fun ItemList(items: List<Item>, selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    LazyColumn(modifier = Modifier) {
        items(items.size) { index ->
            Text(
                text = items[index].title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemSelected(index) }
                    .padding(16.dp),
            )
        }
    }
}

@Composable
fun ItemDetail(item: Item) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(text = item.title, fontSize = 30.sp)
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.description)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Oppgave4Theme {
        MainScreen()
    }
}
