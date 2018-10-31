package ru.hse.spb.kazakov

import com.mongodb.MongoClient
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id
import ru.hse.spb.kazakov.nlp.LocationContext
import ru.hse.spb.kazakov.nlp.LocationRecognizer
import java.io.File

class BookHandler(host: String, port: Int, dbName: String) {
    private val pipeliner = LocationRecognizer()
    private val datastore: Datastore

    init {
        val morphia = Morphia()
        morphia.mapPackage("ru.hse.spb.kazakov")
        datastore = morphia.createDatastore(MongoClient(host, port), dbName)
        datastore.ensureIndexes()
    }

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
                datastore.save(bookLoc)
            }
            section = reader.readNextSection()
            sectionNumber++
        }
    }

    @Entity
    data class BookLocation(
        var title: String = "", var author: String = "",
        var position: Int = 0, var location: LocationContext = LocationContext(), @Id var id: Int = 0
    )
}