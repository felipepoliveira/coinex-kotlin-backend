package io.felipepoliveira.coinex.utils.text

import kotlin.random.Random

val LETTERS_LOWER_CASE = "abcdefghijklmnopqrstuvwxyz"
val LETTERS_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
val DIGITS = "0123456789"
val LETTERS_AND_DIGITS_UPPER_AND_LOWER_CASE: String = "$LETTERS_LOWER_CASE$LETTERS_UPPER_CASE$DIGITS"

fun generateRandomText(seed: String, amountOfCharacters: Int): String {
    // parameters validation
    if (seed.isEmpty()) {
        throw Exception("Seed can not be empty")
    }
    if (amountOfCharacters < 0) {
        throw Exception("Parameter amountOfLetters should be >= 0. $amountOfCharacters given.")
    }
    if (amountOfCharacters < 1) {
        return ""
    }

    val rand = Random(System.currentTimeMillis())
    val stringBuffer = StringBuffer()
    for (i in 0..amountOfCharacters) {
        stringBuffer.append(seed[rand.nextInt(seed.length)])
    }

    return stringBuffer.toString()
}