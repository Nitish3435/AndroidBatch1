package com.ingenioustech.textsandmodifiers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingenioustech.textsandmodifiers.ui.theme.TextsAndModifiersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextsAndModifiersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}


@Composable
fun MyApp() {
    Column(modifier = Modifier
        .border( // Defines the border properties
            width = 10.dp, // Border thickness
            color = Color.Black// Border color
        )
    ){
        Row (modifier = Modifier
            .border( // Defines the border properties
                width = 2.dp, // Border thickness
                color = Color.Black// Border color
            )
        ){
            Column (modifier = Modifier
                .border( // Defines the border properties
                    width = 2.dp, // Border thickness
                    color = Color.Black// Border color
                )
            ){

                Row (modifier = Modifier
                    .border( // Defines the border properties
                        width = 2.dp, // Border thickness
                        color = Color.Black// Border color
                    )
                ){
                    Column (modifier = Modifier
                        .border( // Defines the border properties
                            width = 2.dp, // Border thickness
                            color = Color.Black// Border color
                        )
                    ){
                        Text(
                            text = "Hello Adarsh!",
                            modifier = Modifier.padding(20.dp),
                            fontSize = 25.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column (modifier = Modifier
                        .border( // Defines the border properties
                            width = 2.dp, // Border thickness
                            color = Color.Black// Border color
                        )
                    ){
                        Text(
                            text = "Hello ITW!",
                            modifier = Modifier.padding(20.dp),
                            fontSize = 25.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.ExtraBold,
                            textDecoration = TextDecoration.LineThrough,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
                Row (modifier = Modifier
                    .border( // Defines the border properties
                        width = 2.dp, // Border thickness
                        color = Color.Black// Border color
                    )
                ){
                    Column (modifier = Modifier
                        .border( // Defines the border properties
                            width = 2.dp, // Border thickness
                            color = Color.Black// Border color
                        )
                    ){
                        Text(
                            text = "Hello Nitish!",
                            modifier = Modifier.padding(20.dp),
                            fontSize = 45.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Cyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row (modifier = Modifier
                    .border( // Defines the border properties
                        width = 2.dp, // Border thickness
                        color = Color.Black// Border color
                    )
                ){
                    Text(
                        text = "I am new to Android Development",
                        modifier = Modifier
                            .padding(20.dp),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }
        Row (modifier = Modifier
            .border( // Defines the border properties
                width = 2.dp, // Border thickness
                color = Color.Black// Border color
            )
        ){
            Column (modifier = Modifier
                .border( // Defines the border properties
                    width = 2.dp, // Border thickness
                    color = Color.Black// Border color
                )
            ){
                Row(modifier = Modifier
                    .padding(20.dp)
                    .border( // Defines the border properties
                        width = 2.dp, // Border thickness
                        color = Color.Black// Border color
                    )
                ){
                    Column {
                        Text(text = "A new row")
                    }
                    Column {
                        Text(text= "And it's colummn")
                    }
                }
            }
            Column (modifier = Modifier
                .border( // Defines the border properties
                    width = 2.dp, // Border thickness
                    color = Color.Black// Border color
                )
            ){
                Text(text = "Not it's column")
            }
        }
    }
}


@Preview
@Composable
fun MyAppPreview() {
    MyApp()
}