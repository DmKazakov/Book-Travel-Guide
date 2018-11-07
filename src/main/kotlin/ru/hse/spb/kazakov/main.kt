package ru.hse.spb.kazakov

import java.io.File

/**
 * Recursively traverses directories and stores location extracted from found epub files in database.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No epub file specified.")
        return
    }

    val bookLocStore = BookLocationStore("BookTravelGuide", "127.0.0.1", 27017)
    val bookHandler = BookHandler(bookLocStore)
    File(args[0])
        .walk()
        .filter { !it.name.contains("images") && it.isFile }
        .forEach { bookHandler.processBook(it) }
}
