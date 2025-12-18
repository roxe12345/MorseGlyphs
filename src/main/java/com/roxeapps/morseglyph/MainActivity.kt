package com.roxeapps.morseglyph

import android.content.ComponentName
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roxeapps.morseglyph.ui.theme.MorseGlyphTheme
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.nothing.ketchum.Common
import com.nothing.ketchum.Glyph
import com.nothing.ketchum.GlyphException
import com.nothing.ketchum.GlyphManager
import com.roxeapps.morseglyph.ui.theme.NothingRed

class MainActivity : ComponentActivity() {
    companion object{var mGM: GlyphManager? = null}
    object GlyphA {var isGlyphA by mutableStateOf(false)}
    object GlyphC {var isGlyphC by mutableStateOf(false)}
    object GlyphB {var isGlyphB by mutableStateOf(false)}
    object isChecked{var checked by mutableStateOf(false)}
    object isSoundChecked{var isCheckedSound by mutableStateOf(true)}
    object morseCode{var isMorse by mutableStateOf(false)}
    object MorseSound{var isSound by mutableStateOf(true)}
    object Version{var appVer = "MorseGlyphs DEBUG-2.0.0"}

    private var sessionReady by mutableStateOf(false)

    private val mCallback = object : GlyphManager.Callback {
        override fun onServiceConnected(name: ComponentName?) {
            Log.i("[MorseGlyphs]", Build.MODEL)
            // Register correct devices
            when {
                Common.is24111() -> mGM?.register(Glyph.DEVICE_24111) // 3a and 3a Pro
                Common.is23113() -> mGM?.register(Glyph.DEVICE_23113) // 2a plus
                Common.is23111() -> mGM?.register(Glyph.DEVICE_23111) // 2a
                Common.is22111() -> mGM?.register(Glyph.DEVICE_22111) // 2
                Common.is20111() -> mGM?.register(Glyph.DEVICE_20111) // 1
            }
            try {
                mGM?.openSession()
                sessionReady = true
                Log.d("[MorseGlyphs]", "Session opened")
            } catch (e: GlyphException) {
                sessionReady = false
                Log.e("[MorseGlyphs]", "Error opening session: ${e.message}", e)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            try {
                mGM?.closeSession()
            } catch (e: GlyphException) {
                Log.e("[MorseGlyphs]", "Error closing session: ${e.message}", e)
            }
            sessionReady = false
        }
    }

    object GlyphController{
        fun flashGlyphA(){ //DOT DOT DOT DOT DOT
            val builder = mGM?.glyphFrameBuilder

            if (Build.MODEL.contains("059") || Build.MODEL.contains("059P")) {
                    val frame = builder
                        ?.buildChannelA()
                        ?.build()

                    if (frame != null) {
                        mGM?.animate(frame)

                        GlyphA.isGlyphA = true
                        Log.i("MorseGlyphs", "GlyphA -> true")

                        try {
                            mGM?.toggle(frame)   // Light ON
                            Handler(Looper.getMainLooper()).postDelayed({
                                try {
                                    mGM?.turnOff()
                                    GlyphA.isGlyphA = false
                                    Log.i("MorseGlyphs", "GlyphA -> false")


                                } catch (e: GlyphException) {
                                    Log.e("GlyphManager", "Failed to turn off: ${e.message}")
                                }
                            }, 60)
                        } catch (e: GlyphException) {
                            Log.e("GlyphManager", "Toggle failed: ${e.message}")
                        }
                }
            }
            else if (Build.MODEL.contains("142") || Build.MODEL.contains("142P")){
                    val frame = builder
                        ?.buildChannelB()
                        ?.build()
                    if (frame != null) {
                        mGM?.animate(frame)
                        GlyphB.isGlyphB = true
                        Log.i("MorseGlyphs", "GlyphB -> true(2a)")

                        try {
                            mGM?.toggle(frame)   // Light ON
                            Handler(Looper.getMainLooper()).postDelayed({
                                try {
                                    mGM?.turnOff()
                                    GlyphB.isGlyphB = false
                                    Log.i("MorseGlyphs", "GlyphB -> false(2a)")
                                } catch (e: GlyphException) {
                                    Log.e("GlyphManager", "Failed to turn off: ${e.message}")
                                }
                            }, 60)
                        } catch (e: GlyphException) {
                            Log.e("GlyphManager", "Toggle failed: ${e.message}")
                        }
                    }


            }else if (Build.MODEL.contains("065") /* Phone 2 */ || Build.MODEL.contains("063") /* Phone 1 */){
                val frame = builder
                    ?.buildChannelE()
                    ?.build()
                if (frame != null) {
                    mGM?.animate(frame)

                    GlyphB.isGlyphB = true
                    Log.i("MorseGlyphs", "GlyphE -> true(phone (2))")

                    try {
                        mGM?.toggle(frame)   // Light ON
                        Handler(Looper.getMainLooper()).postDelayed({
                            try {
                                mGM?.turnOff()
                                GlyphB.isGlyphB = false
                                Log.i("MorseGlyphs", "GlyphE -> false(2)")


                            } catch (e: GlyphException) {
                                Log.e("GlyphManager", "Failed to turn off: ${e.message}")
                            }
                        }, 60)
                    } catch (e: GlyphException) {
                        Log.e("GlyphManager", "Toggle failed: ${e.message}")
                    }
                }
            }
    }

    fun flashGlyphC(){ //DASH DASH DASH DASH DASH
        if (Build.MODEL.contains("065")/* Phone 2 */ || Build.MODEL.contains("063") /* Phone 1 */){
            val builder = mGM?.glyphFrameBuilder
            val frame = builder
                ?.buildChannelD()
                ?.build()
            if (frame != null) {
                mGM?.animate(frame)

                GlyphC.isGlyphC = true
                Log.i("MorseGlyphs", "GlyphD -> true(phone (1/2))")

                try {
                    mGM?.toggle(frame)   // Light ON
                    Handler(Looper.getMainLooper()).postDelayed({
                        try {
                            mGM?.turnOff()
                            GlyphB.isGlyphB = false
                            Log.i("MorseGlyphs", "GlyphD -> false(1/2)")


                        } catch (e: GlyphException) {
                            Log.e("GlyphManager", "Failed to turn off: ${e.message}")
                        }
                    }, 180)
                } catch (e: GlyphException) {
                    Log.e("GlyphManager", "Toggle failed: ${e.message}")
                }

            }
            }else {


                val builder = mGM?.glyphFrameBuilder

                val frame = builder
                    ?.buildChannelC()
                    ?.build()
                if (frame != null) {
                    try {
                        mGM?.animate(frame)
                        GlyphC.isGlyphC = true
                        Log.i("MorseGlyphs", "GlyphC -> true")


                        mGM?.toggle(frame)   // Light ON
                        Handler(Looper.getMainLooper()).postDelayed({
                            try {
                                mGM?.turnOff()
                                GlyphC.isGlyphC = false
                                if (GlyphC.isGlyphC == false) {
                                    Log.i("MorseGlyphs", "GlyphC -> false")
                                }
                            } catch (e: GlyphException) {
                                Log.e("GlyphManager", "Failed to turn off: ${e.message}")
                            }
                        }, 180)
                    } catch (e: GlyphException) {
                        Log.e("GlyphManager", "Toggle failed: ${e.message}")
                    }
                } } } }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGM = GlyphManager.getInstance(applicationContext)
        mGM?.init(mCallback)
        enableEdgeToEdge()
        setContent {
            MorseGlyphTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {

    val clipboardManager = LocalClipboardManager.current
    val customFont = FontFamily(Font(R.font.leddot))
    var isHome by remember { mutableStateOf(true) }
    var isInfo by remember {mutableStateOf(false)}
    var isSettings by remember {mutableStateOf(false)}
    val supportedModels = listOf("059", "059P", "142", "142P", "065", "063")
    val isSupported = supportedModels.any { Build.MODEL.contains(it) }

    /*if (!isSupported)    {
        isHome = false
        isInfo = false
        Column(Modifier.fillMaxSize(), horizontalAlignment =  Alignment.CenterHorizontally) {
            Spacer(Modifier.height(50.dp))
            Text(
                text = "ERROR",
                color = Color.Red,
                fontFamily = customFont,
                fontSize = 40.sp
            )
            Text(
                text = "Your phone is not supported.",
                fontFamily = customFont,
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Supported Phones:",
                fontFamily = customFont,
                fontSize = 25.sp,
                color = Color.White
            )
            Text(
                text = "Nothing Phone (1)",
                fontFamily = customFont,
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                text = "Nothing Phone (2)/(2a)/(2a) Plus",
                fontFamily = customFont,
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                text = "Nothing Phone (3a)/(3a) Pro",
                fontFamily = customFont,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }*/
    var textUnicode by remember { mutableStateOf("") }
    if (isHome == true) {
        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.
                width(430.dp).
                height(70.dp).
                offset(y=25.dp).
                background(color = Color(0xFF171717),
                    shape = RoundedCornerShape(48.dp)).
                shadow(2.dp, shape = RoundedCornerShape(48.dp))
            ) {}
            Row(modifier = Modifier.fillMaxSize().padding(26.dp)) {

                IconButton(
                    onClick = {
                        isHome = false
                        isInfo = true
                    },
                    Modifier.
                    padding(20.dp).
                    offset(y = (-10).dp, x = (-40).dp).
                    background(
                        Color(color = 0xFF303030),
                        shape = RoundedCornerShape(36.dp)
                    ).
                    shadow(2.dp, RoundedCornerShape(40.dp)).
                    width(130.dp)
                )
                {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.
                        size(30.dp).
                        offset(x = (-45).dp)
                    )
                    Text(
                        text = "Info",
                        fontFamily = customFont,
                        fontSize = 25.sp,
                        color = Color.White,
                        modifier = Modifier.
                        offset(10.dp)
                    )
                }
                IconButton(
                    onClick = {
                        isHome=false
                        isInfo=false
                        isSettings=true
                    },
                    Modifier.
                    width(50.dp).offset(y=10.dp, x =150.dp)
                )
                {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.DarkGray,
                                shape = RoundedCornerShape(56.dp)
                            )
                            .padding(6.dp),
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                    )
                }
            }





            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(100.dp))

                Text(
                    text = "MorseGlyphs",
                    fontFamily = customFont,
                    color = Color.White,
                    fontSize = 46.sp
                )
                val morse = textUnicode
                // Morse->letters
                val morseMap = mapOf(
                    ".-" to "A", "-..." to "B", "-.-." to "C", "-.." to "D",
                    "." to "E", "..-." to "F", "--." to "G", "...." to "H",
                    ".." to "I", ".---" to "J", "-.-" to "K", ".-.." to "L",
                    "--" to "M", "-." to "N", "---" to "O", ".--." to "P",
                    "--.-" to "Q", ".-." to "R", "..." to "S", "-" to "T",
                    "..-" to "U", "...-" to "V", ".--" to "W", "-..-" to "X",
                    "-.--" to "Y", "--.." to "Z",
                    //morse->n
                    "-----" to "0",
                    ".----" to "1",
                    "..---" to "2",
                    "...--" to "3",
                    "....-" to "4",
                    "....." to "5",
                    "-...." to "6",
                    "--..." to "7",
                    "---.." to "8",
                    "----." to "9",
                    "/" to " "
                )


                val morseList = morse.split(" ")
                val result = if (MainActivity.morseCode.isMorse==false){
                    morseList.joinToString("") { code ->
                        morseMap[code] ?: code
                    }
                }else{
                    morse
                }

                Spacer(Modifier.height(36.dp))

                Text(
                    text = result,
                    color = Color.White,
                    fontFamily = customFont,
                    fontSize = 26.sp
                )
                Spacer(Modifier.height(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val imageResource = when {
                        Build.MODEL.contains("059") || Build.MODEL.contains("059P") -> when {
                            MainActivity.GlyphA.isGlyphA == true -> R.drawable.glypha
                            MainActivity.GlyphC.isGlyphC == true -> R.drawable.glyphc
                            else -> R.drawable.emptyglyph
                        }

                        Build.MODEL.contains("142") || Build.MODEL.contains("142P") -> when {
                            MainActivity.GlyphB.isGlyphB == true -> R.drawable.glyphb2a
                            MainActivity.GlyphC.isGlyphC == true -> R.drawable.glyphc2a
                            else -> R.drawable.empty2a
                        }
                        Build.MODEL.contains("065") || Build.MODEL.contains("063") -> when {
                            MainActivity.GlyphB.isGlyphB == true /* dot */ -> R.drawable.phone2glyphdot
                            MainActivity.GlyphC.isGlyphC == true /* dash */ -> R.drawable.phone2glyphdash
                            else -> R.drawable.phone2glyphempty
                        }
                        else -> {
                            R.drawable.glyphempty

                        }
                    }

                    Image(
                        painter = painterResource(imageResource),
                        contentDescription = null
                    )
                }

                Spacer(Modifier.height(150.dp))
                val morsePlayer = remember { MorseSoundPlayer() }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    MorseButton(
                        text = ".",
                        fontFamily = FontFamily.SansSerif

                    ) { value ->
                        textUnicode += value

                        morsePlayer.playDot()
                        MainActivity.GlyphController.flashGlyphA()


                    }
                    MorseButton(
                        text = "-",
                        fontFamily = FontFamily.SansSerif
                    ) { value ->
                        textUnicode += value
                        morsePlayer.playDash()
                        MainActivity.GlyphController.flashGlyphC()
                    }
                    MorseButton(
                        text = "/",
                        fontFamily = FontFamily.Default
                    ) { value -> textUnicode += " / " }
                    MorseButton(
                        text = " ",
                        fontFamily = customFont
                    ) { value -> textUnicode += " " }

                }

                Row {
                    Button(
                        onClick = {
                            if (textUnicode.endsWith(" / ")) {
                                textUnicode = textUnicode.dropLast(3)
                            } else if (textUnicode.isNotEmpty()) {
                                textUnicode = textUnicode.dropLast(1)
                            }
                        },
                        shape = RoundedCornerShape(size = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        modifier = Modifier.size(width = 60.dp, height = 60.dp)
                    ) {
                        Text(
                            text = "⌫",
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontSize = 30.sp,
                            modifier = Modifier.offset((-10).dp)
                        )

                    }
                    Spacer(Modifier.width(5.dp))
                    Button(
                        onClick = {
                            textUnicode = ""
                        },
                        shape = RoundedCornerShape(size = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        modifier = Modifier.size(width = 60.dp, height = 60.dp)
                    ) {
                        Text(
                            text = "C",
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontSize = 30.sp,
                            modifier = Modifier.offset((-5).dp)
                        )

                    }
                    Spacer(Modifier.height(20.dp))
                }
                Row(modifier = Modifier.offset(x=(-60).dp)) {
                    Button(onClick = {
                        clipboardManager.setText(AnnotatedString(morse))

                    },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.offset(x = (-10).dp, y = 10.dp).
                        width(125.dp)
                            .height(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = null,
                            tint = Color.White,
                        )
                        Text(
                            text = "copy",
                            fontFamily = customFont,
                            color = Color.White,
                            fontSize = 17.sp

                        )
                    }
                    Button(
                        onClick = { morsePlayer.playMorseSequence(textUnicode) },
                        shape = RoundedCornerShape(size = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51FF11)),
                        modifier = Modifier.size(width = 60.dp, height = 60.dp)
                    ) {
                        Text(
                            text = "▶",
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontSize = 30.sp,
                            modifier = Modifier.offset((-8).dp, y =(-2).dp).padding(1.dp)
                        )
                    }
                    Spacer(Modifier.width(5.dp))
                    Button(
                        onClick = { morsePlayer.stop() },
                        shape = RoundedCornerShape(size = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5430)),
                        modifier = Modifier.size(width = 60.dp, height = 60.dp)
                    ) {
                        Text(
                            text = "■",
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontSize = 30.sp,
                            modifier = Modifier.offset((-6).dp, y =(-2).dp)
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))
                Text(
                    text = MainActivity.Version.appVer,
                    fontFamily = FontFamily(Font(R.font.ntype)),
                    color = Color.DarkGray,
                    fontSize = 13.sp
                )
            }

        }
    }


    //----------isInfo-----------//


    if (isHome==false && isInfo == true){
        BackHandler(enabled = true) {
            isHome=true
            isInfo=false
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
            modifier = Modifier.
            fillMaxSize()
        ) {
            IconButton(
                onClick = {
                    isHome = true
                },
                modifier = Modifier.
                padding(26.dp).
                offset(y = 10.dp, x = (-20).dp).
                background(
                    Color(0xFFFF1C1C),
                    shape = RoundedCornerShape(36.dp)
                ).
                shadow(2.dp, RoundedCornerShape(40.dp)).
                width(130.dp)
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
            Text(
                text = "INFO",
                fontFamily = customFont,
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.offset(x = (-30).dp, y = 50.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier) {
            Spacer(Modifier.height(100.dp))

            Text(
                text = "Morse code is a telecommunications method that encodes text as a series of short and long signals, known as \"dots\" and \"dashes\". Developed in the 1830s by Samuel Morse and his collaborators, it uses sequences of dots, dashes, and spaces to represent letters, numbers, and punctuation.",
                fontFamily = FontFamily(Font(R.font.ntype)),
                color = Color(0xFF8B8B8B),
                fontSize = 20.sp

            )


            Box(modifier = Modifier.
            offset(x = 50.dp).
            height(240.dp).
            width(300.dp).
            background(
                Color(0xFF151515),
                shape = RoundedCornerShape(26.dp)
            ).
            shadow(
                2.dp,
                shape = RoundedCornerShape(80.dp), clip = true
            ).
            padding(20.dp),

                    contentAlignment = Alignment.Center,
                ){
                Text(
                    text = "A = .-  B = -...  C = -.-.  D = -..\n" +
                        "E = .   F = ..-.  G = --.  H = ....\n" +
                        "I = ..  J = .--- K = -.-  L = .-..\n" +
                        "M = --  N = -.  O = ---  P = .--.\n" +
                        "Q = --.-  R = .-.  S = ...  T = -\n" +
                        "U = ..-  V = ...-  W = .--  X = -..-\n" +
                        "Y = -.--   Z = --..",
                    fontFamily = FontFamily(Font(R.font.ntype)),
                    color = Color.White,
                    fontSize = 25.sp,
                    )
            }
            Text(
                text = "How to use MorseGlyphs?",
                fontFamily = FontFamily(Font(R.font.ntype)),
                color = Color.White,
                fontSize = 36.sp
            )
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(Modifier.height(10.dp))
                Text(
                    fontFamily = FontFamily(Font(R.font.ntype)),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    text = "You can see that there is an empty button, you can use this button to tell the app that you are typing a new letter. If you do not use it, the app might think that you are still typing the same letter."
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    fontFamily = FontFamily(Font(R.font.ntype)),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 13.sp,
                    text = "You can also see that there is a  \"/\" button, you can use this button to add a space between words, you need to use this so you can type different words."
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    fontFamily = FontFamily(Font(R.font.ntype)),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    text = "You can see that there is a  \"⌫\" button, you can use this button to remove the last dit, dah or space so you can edit what you have written. The \"C\" button clears everything you've entered in Morse Code"
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    fontFamily = customFont,
                    color = Color.Green,
                    fontSize = 12.sp,
                    lineHeight = 10.sp,
                    text = "The \"▶\" button plays what you have written using Morse Code.\n"
                )
                Text(
                    fontFamily = customFont,
                    color = Color.Red,
                    fontSize = 12.sp,
                    lineHeight = 10.sp,
                    text = "The \"■\" button stops playback."
                )


            }
            Spacer(Modifier.height(102.dp))
            Text(
                text = MainActivity.Version.appVer,
                fontFamily = FontFamily(Font(R.font.ntype)),
                color = Color.DarkGray,
                fontSize = 8.sp,
                modifier = Modifier.
                offset(x=(150.5).dp)
            )
        }


    }
    }









    if (isSettings==true&& isHome==false && isInfo==false){
        Composables.settingsUI(

            iconButtonOnclick = {
                isSettings=false
                isInfo=false
                isHome=true
            }, backhandler = {
                isHome =true
                isInfo =false
                isSettings=false
            }

        )
    }
}




@Composable
fun MorseButton(
    text: String,
    fontFamily: FontFamily,
    onClick: (String) -> Unit
) {
    Button(
        onClick = {onClick(text)},
        shape = RoundedCornerShape(size = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = NothingRed),
        modifier = Modifier.size(width = 60.dp, height = 60.dp)
    ) {
        Text(
            text = text,
            fontFamily = fontFamily,
            color = Color.White,
            fontSize = 36.sp,
        )

    }
    Spacer(Modifier.width(5.dp))
}
@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun MainAppPreview(){
    MainApp()
}