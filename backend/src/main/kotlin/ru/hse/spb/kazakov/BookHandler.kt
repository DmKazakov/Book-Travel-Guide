package ru.hse.spb.kazakov

import ru.hse.spb.kazakov.mongo.BookLocation
import ru.hse.spb.kazakov.mongo.BookLocationStore
import java.io.File

class BookHandler(private val bookLocStore: BookLocationStore) {
    private val pipeliner = LocationRecognizer()

    /**
     * Extracts locations from specified epub and stores them in database.
     */
    fun processBook(book: File) {
        val reader = EpubReader(book.absolutePath)

        var section = reader.readNextSection()
        var sectionNumber = 0
        while (section != null) {
            pipeliner.extractLocations(section).forEach {
                val id = book.parentFile.name.toInt()
                val bookLoc = BookLocation(reader.title, reader.creator, sectionNumber, it, id)
                bookLocStore.save(bookLoc)
            }
            section = reader.readNextSection()
            sectionNumber++
        }
    }
}
