package com.example.jettip.widgets

import androidx.compose.foundation.clickable



import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.*
val IconbuttonSizeModifier=Modifier.size(25.dp)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundIconButton(
    imageVector:ImageVector,
    modifier: Modifier=Modifier,
    onClick:()->Unit,
    tint:Color=Color.Black.copy(alpha = 0.8f),
    backgroundColor:Color=MaterialTheme.colorScheme.background,

){
Card(modifier = modifier
    .padding(all = 10.dp)
    .clickable { onClick.invoke() }
    .then(IconbuttonSizeModifier),
    shape = CircleShape,
    )

{

        Icon(imageVector = imageVector, contentDescription ="",tint=tint )

}
}