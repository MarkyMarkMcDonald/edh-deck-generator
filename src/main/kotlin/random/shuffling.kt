package random

import java.util.*

fun <E> Collection<E>.shuffle(): Collection<E> {
    val randomizedList = ArrayList(this)
    Collections.shuffle(randomizedList)
    return randomizedList
}

fun <E> Collection<E>.sample(amount: Int): Collection<E> {
    if (amount <= 0) return emptyList()
    return this.shuffle().take(amount)
}

fun <E> Collection<E>.sample(): E {
    return this.shuffle().first()
}