package com.example.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.jettip.components.InputField
import com.example.jettip.ui.theme.JetTipTheme
import com.example.jettip.util.calculateTotalPerPerson
import com.example.jettip.util.calculateTotalTip
import com.example.jettip.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp{

//                TopHeader()
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()

        }
    }

}

@Composable
fun  TopHeader(totalPerPerson:Double=0.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(170.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(10.dp))),
        color = Color(0xFFE9D7F7)

//        .clip(shape = RoundedCornerShape(corner = CornerSize(10.dp)))

    ) {
        Column(
            modifier=Modifier.padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total="%.2f".format(totalPerPerson)
            Text(text="Total Per Person", style = MaterialTheme.typography.displaySmall,color = Color.Black)
            Text(text="$$total",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        
    }
}

@Composable
fun MainContent(){
    val splitByState= remember {
        mutableStateOf(1)
    }

    val tipAmountState=remember{
        mutableStateOf(0.0)
    }

    val totalPerPerson= remember {
        mutableStateOf(0.0)
    }


    val range =IntRange(start = 1, endInclusive = 100)

Column(modifier = Modifier.padding(all = 10.dp)) {
    TopHeader(totalPerPerson=totalPerPerson.value)
    Spacer(modifier = Modifier.height(10.dp))
    BillFrom(
       splitByState=splitByState,
        range=range,
       tipAmountState = tipAmountState,
       totalPerPersonState =totalPerPerson
    ) {
        Log.d("", "Amount $it")

    }
}


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillFrom(
    modifier: Modifier=Modifier,
    range: IntRange=1..100,
    splitByState:MutableState<Int>,
    tipAmountState:MutableState<Double>,
    totalPerPersonState:MutableState<Double>,
    onValChange:(String)-> Unit={} ){
    val totalBillState= remember {
        mutableStateOf("")
    }
    val validState= remember( totalBillState.value) {
totalBillState.value.trim().isNotEmpty()
    }


    val keyboardController=LocalSoftwareKeyboardController.current
    val sliderPositionValue= remember {
        mutableStateOf(0f)
    }
    val tipPercentage=(sliderPositionValue.value *100).toInt()

    Surface(modifier = modifier
        .padding(all = 2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp),),
        border = BorderStroke(width = 2.dp, color = Color.LightGray)

    ) {

       Column(
           modifier=modifier.padding(6.dp),
           verticalArrangement = Arrangement.Top,
           horizontalAlignment = Alignment.Start
           ) {

           InputField(
               valueState = totalBillState,
               labelId ="Enter Bill" ,
               enabled = true,
               isSingleLine =true,
               onAction = KeyboardActions{
                   if(!validState) return@KeyboardActions
                   onValChange(totalBillState.value.trim())
                   keyboardController?.hide()
               }
           )
           if (validState){
              Row(
                  modifier= modifier
                      .padding(3.dp)
                      .fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                  Text(text = "Split",modifier=Modifier.align(alignment = Alignment.CenterVertically))
//              Spacer(modifier = Modifier.fillMaxWidth( fraction = 0.66f))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        RoundIconButton(

                            imageVector = Icons.Default.Remove,
                            onClick = {
                                splitByState.value= if(splitByState.value>1)splitByState.value-1 else 1
                                totalPerPersonState.value= calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage= tipPercentage,
                                )

                            },


                        )
                        Text(text = "${splitByState.value}")
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if(splitByState.value<range.last){
                                    splitByState.value++
                                    totalPerPersonState.value= calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitByState.value,
                                        tipPercentage= tipPercentage,
                                    )
                                }


                            },


                            )



                    }
              }
           }else{
               Box() {
                   
               }

           }
           Row(
               modifier = modifier
                   .padding(3.dp)
                   .fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween
           ){
               Text(text = "Tip:")
               Text(text = "$ ${tipAmountState.value}")
           }
           Column(
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               Text(text = "$tipPercentage %")
               Spacer(modifier = Modifier.height(20.dp))
               Slider(value = sliderPositionValue.value, onValueChange = {
                   sliderPositionValue.value=it
                   tipAmountState.value= calculateTotalTip(
                       totalBill= totalBillState.value.toDouble(),
                     tipPercentage=  tipPercentage,
                   )
                   totalPerPersonState.value= calculateTotalPerPerson(
                       totalBill = totalBillState.value.toDouble(),
                       splitBy = splitByState.value,
                     tipPercentage= tipPercentage,
                   )
               },

                   modifier=Modifier.padding(horizontal = 16.dp),
                   steps = 5,
                   onValueChangeFinished = {

                   }
                   )
           }
       }



    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipTheme {
        MyApp{

            MainContent()
        }
    }
}