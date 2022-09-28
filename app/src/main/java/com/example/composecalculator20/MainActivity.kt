package com.example.composecalculator20

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composecalculator20.ui.theme.ComposeCalculator20Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCalculator20Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipScreen()
                }
            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TipScreen(){

    var bill:Int by remember { mutableStateOf(0) }
    var tip:Double by remember { mutableStateOf(0.0)}

    var scope = rememberCoroutineScope()
    var isShowingSnackbar by remember { mutableStateOf(false) }
    var scaffoldState = rememberScaffoldState(snackbarHostState = SnackbarHostState())

    Scaffold(
        scaffoldState = scaffoldState
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TipTitle()
            TextField(
                value = if(bill == 0) "" else bill.toString(),
                label = {Text("Cost of Service")},
                onValueChange = {
                    bill = it.toInt()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = if(tip == 0.0) "" else "$tip",
                onValueChange = {
                    tip = it.toDouble()
                },
                label = { Text("Tip %") } ,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        isShowingSnackbar = !((bill == 0) && (tip == 0.0))
                        Log.d("onDone", "$isShowingSnackbar")
                    }
                )
            )

        }
    }
    LaunchedEffect(key1 = isShowingSnackbar){
        Log.d("LaunchedEffect","shot")
        scope.launch {
            if (isShowingSnackbar) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "${calculate(bill, tip/100)}",
                    actionLabel = "TIP"
                )
            }
            isShowingSnackbar = false
        }
    }
}

@Composable
fun TipTitle(){
    Text(
        text = "Calculate Tip",
        style = MaterialTheme.typography.h4
    )
}


fun calculate(a: Int, b: Double):Double {
    return (a*b)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCalculator20Theme {
        TipScreen()
    }
}