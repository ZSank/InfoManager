package com.app.infomanager.util

import kotlin.math.pow
import kotlin.random.Random

fun getRandomNumber(digits: Int = 4) =
	Random.nextInt(10.0.pow(digits - 1).toInt(), 10.0.pow(digits).toInt())

fun getRandomWord(len: Int = 3): String {
	val chars = ('a'..'z')
	return (1..len)
		.map { chars.random() }
		.joinToString("")
}

fun String.toIntOrDef(def: Int = 0): Int {
	return this.toIntOrNull() ?: def
}

fun String.toLongOrDef(def: Long = 0): Long {
	return this.toLongOrNull() ?: def
}
