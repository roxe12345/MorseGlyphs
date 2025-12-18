package com.roxeapps.morseglyph

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlin.math.sin

class MorseSoundPlayer(
    private val sampleRate: Int = 44100,
    private val freq: Int = 800,
) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null


    /** Generate PCM sine tone */
    private fun generateTone(durationMs: Int): ShortArray {
        val count = (sampleRate * (durationMs / 1000.0)).toInt()
        return ShortArray(count) { i ->
            val angle = 2.0 * Math.PI * i / (sampleRate / freq)
            (sin(angle) * Short.MAX_VALUE).toInt().toShort()
        }
    }


    private suspend fun playSymbol(duration: Long) {
        val buffer = generateTone(duration.toInt())
        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            buffer.size * 2,
            AudioTrack.MODE_STATIC
        )
        track.write(buffer, 0, buffer.size)
        track.play()

        delay(duration)
        track.release()

    }

    fun playDot() {
        if (MainActivity.MorseSound.isSound==true){
        enqueue {
            playSymbol(60)
            delay(60)
        }
        }
    }

    fun playDash() {
        if (MainActivity.MorseSound.isSound==true){
            enqueue {
            playSymbol(60*3)
            delay(60)
        }
    }
    }

    fun playMorseSequence(morse: String) {
        stop()
            enqueue {

            val words = morse.trim().split(" / ")
            for ((wIndex, word) in words.withIndex()) {
                val letters = word.split(" ")
                for ((lIndex, letter) in letters.withIndex()) {
                    for ((sIndex, symbol) in letter.withIndex()) {
                        when (symbol) {
                            '.' -> {
                                if (MainActivity.MorseSound.isSound==true){

                                    playSymbol(60)}
                                MainActivity.GlyphController.flashGlyphA()
                            }
                            '-' -> {
                                if (MainActivity.MorseSound.isSound==true){

                                    playSymbol(60*3)}
                                MainActivity.GlyphController.flashGlyphC()
                            }
                        }
                        if (sIndex != letter.lastIndex) delay(60)
                    }
                    if (lIndex != letters.lastIndex) delay(60*3)
                }
                if (wIndex != words.lastIndex) delay(60*7)
            }
        }

    }

    /** Queue helper */
    private fun enqueue(block: suspend () -> Unit) {
        job = scope.launch {
            block()
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}
