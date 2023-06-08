package it.dario.malaapplicazione.data

import java.util.concurrent.atomic.AtomicInteger

object IdGenerator {
    private val counter = AtomicInteger()

    val getNext get() = counter.incrementAndGet()
}