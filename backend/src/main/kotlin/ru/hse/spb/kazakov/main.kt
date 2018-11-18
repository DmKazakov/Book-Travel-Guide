package ru.hse.spb.kazakov

import java.io.File

fun main(args: Array<String>) {
    if (args.size < 3) {
        println("No epub file, databse IP or log file specified.")
        return
    }

    val bookLocStore = BookLocationStore(Datastore.getInstance(args[1]))
    val bookHandler = BookHandler(bookLocStore)
    File(args[0])
        .walk()
        .filter { !it.name.contains("images") && it.isFile }
        .forEach { bookHandler.processBook(it) }
}
