package ru.hse.spb.kazakov

import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No epub file specified.")
        return
    }

    val bookHandler = BookHandler("127.0.0.1", 27017, "BookTravelGuide")
    File(args[0])
        .walk()
        .filter { !it.name.contains("images") && it.isFile }
        .forEach { bookHandler.processBook(it) }
}
