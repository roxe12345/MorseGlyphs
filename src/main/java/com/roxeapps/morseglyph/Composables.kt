package com.roxeapps.morseglyph

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Composables {

    val customFont = FontFamily(Font(R.font.leddot))
    @Composable
    fun settingsUI(backhandler: () -> Unit, iconButtonOnclick: () -> Unit){
        // Log.d("MorseDebug", "settingsUI is now running")
        BackHandler(enabled = true) {
            backhandler()
        }
        Box(modifier = Modifier.fillMaxSize()){


            Box(
                modifier = Modifier.
                width(430.dp).
                height(70.dp).
                    offset(y=25.dp).
                background(color = Color(0xFF151515),
                    shape = RoundedCornerShape(48.dp)).
                shadow(2.dp, shape = RoundedCornerShape(48.dp))
            ) {}
                Row(
                ) {
                    //ICON
                    IconButton(
                        onClick = {
                            iconButtonOnclick()
                        },
                        modifier = Modifier
                            .padding(26.dp)
                            .offset(x = (-20).dp, y = 13.dp)
                            .background(
                                Color(0xFFFF1C1C),
                                shape = RoundedCornerShape(36.dp)
                            )
                            .shadow(2.dp, RoundedCornerShape(40.dp))
                            .width(130.dp)
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.offset(x = (-45).dp)
                        )
                        Text(
                            text = "Back",
                            fontFamily = customFont,
                            fontSize = 25.sp,
                            color = Color.White,
                            modifier = Modifier.offset(10.dp)
                        )
                    }
                    //ICON
                    Text(
                        text = "Settings",
                        fontFamily = customFont,
                        fontSize = 30.sp,
                        color = Color.White,
                        modifier = Modifier.offset(x = (-30).dp, y = 50.dp)
                    )
                }
            Text(
                text = "MorseGlyphs RELEASE-1.0.0",
                fontFamily = FontFamily(Font(R.font.ntype)),
                color = Color.DarkGray,
                fontSize = 10.sp,
                modifier = Modifier.
                offset(x=(150.5).dp)
            )




            @Composable
            fun switchSetting(title: String = "Put a title.",
                              isCheckede: Boolean = false,
                              onChecked: (Boolean) -> Unit,
                              desc: String = "Put a description here.",
                              offset: Dp
            ){
                Box(
                    Modifier.offset(y=offset)
                ){
                Row(
                    modifier = Modifier.offset(y=100.dp)
                ) {
                    Text(title,
                        color = Color.White,
                        fontFamily = customFont,
                        fontSize = 30.sp
                    )



                }
                    Row(
                        modifier = Modifier.offset(y=100.dp)
                    ){
                        Switch(
                            checked = isCheckede,
                            onCheckedChange = onChecked,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Red,
                                checkedTrackColor = Color.DarkGray,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.DarkGray,
                            ),
                            modifier = Modifier.
                            width(100.dp).
                            offset(
                                x=300.dp,
                            )

                        )
                    }
                Text(
                    text = desc,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.ntype)),
                    modifier = Modifier.offset(y= 130.dp ,x = 5.dp),
                    fontSize = 20.sp
                )
            }
            }

            switchSetting(
                title = "Show morse code",
                desc = "Show Morse code instead of Latin",
                isCheckede = MainActivity.isChecked.checked,
                onChecked= {
                    MainActivity.isChecked.checked = it
                    MainActivity.morseCode.isMorse = it
                },
                offset = 50.dp
            )
            Spacer(Modifier.height(100.dp))
            switchSetting(
                isCheckede = MainActivity.isSoundChecked.isCheckedSound,
                onChecked = {
                    MainActivity.isSoundChecked.isCheckedSound = it
                    MainActivity.MorseSound.isSound = it
                },
                offset = 110.dp,
                title = "MORSE SOUND",
                desc = "Control the Morse code sounds")

        }
    }
}
@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun SettingsUIPreview(){
    Composables.settingsUI(backhandler = {}, iconButtonOnclick = {})
}