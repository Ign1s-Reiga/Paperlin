package dev.reiga7953.spikotlin

fun <T> T.not(other: T) = takeUnless { it == other }
fun <T> T.notIn(container: Iterable<T>) = takeUnless { it in container }
