package random

import java.util.*

fun <E> Collection<E>.shuffle(): Collection<E> {
    val randomizedList = ArrayList(this)
    Collections.shuffle(randomizedList)
    return randomizedList
}
