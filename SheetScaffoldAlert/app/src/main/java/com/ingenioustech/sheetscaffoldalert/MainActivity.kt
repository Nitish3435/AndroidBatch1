package com.ingenioustech.sheetscaffoldalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingenioustech.sheetscaffoldalert.ui.theme.SheetScaffoldAlertTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SheetScaffoldAlertTheme {
                ScaffoldExample()

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldExample() {
    Scaffold(
        topBar = {
            TopAppBar(colors = topAppBarColors (
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.error
            ),
                title = { Text(text = "MyTopAppBar")}
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.error
            ) {
                Text(text = "MyBottomAppBar")
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {  }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Chat")
            }
        }


    ) {
        innerPadding ->
        var sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember {
            mutableStateOf(false)
        }
        var showAlert by remember {
            mutableStateOf(false)
        }
        Column(modifier = Modifier.padding(innerPadding)) {

            Text(text = "This is my scaffold Area where I can put a lot of things")
            Button(onClick = { showBottomSheet = true }) {
                Text(text = "Show Bottom Sheet")
            }
            Button(onClick = {showAlert = true}) {
                Text(text = "Show Alert")
            }
        }

        if (showAlert) {
            AlertDialogExample()
        }
        if(showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Column(modifier  = Modifier
                    .padding(10.dp)
                    .fillMaxSize()) {
                    Text(text ="Hi, my name is Swaraj")

                }
            }
        }
    }
}

@Composable
fun AlertDialogExample() {

    AlertDialog(
        icon = { Icon(Icons.Filled.Done, contentDescription = "Done") },

        title = {
            Text(text = "Emergency")
        },
        text = {
            Text(text = "Jetpack Compose is updating it's libraries with more UI elements")
        },
        onDismissRequest = { /*TODO*/ },
        confirmButton = { TextButton(onClick = { println("Clicked on the text button") }) {
            Text(text = "Alright")
        } }
    )
}

@Preview(showBackground = true)
@Composable
fun AlertDialogExamplePreview() {
    AlertDialogExample()
}


@Preview(showBackground = true)
@Composable
fun ScaffoldExamplePreview() {
    SheetScaffoldAlertTheme {
        ScaffoldExample()
    }
}