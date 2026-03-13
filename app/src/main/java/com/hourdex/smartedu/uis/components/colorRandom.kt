package com.hourdex.smartedu.uis.components


val pastels = listOf(
    0xFFFFB3BA, // soft pink
    0xFFFFDFBA, // soft peach
    0xFFFFFFBA, // soft yellow
    0xFFBAFFBA, // soft mint
    0xFFBAE1FF, // soft blue
    0xFFD4BAFF, // soft lavender
    0xFFFFBAF2, // soft magenta
    0xFFBAFFF0, // soft teal
    0xFFFFD6BA, // soft orange
    0xFFE8BAFF, // soft purple
)
fun randomPastelColor(): Long {

    return pastels.random()
}