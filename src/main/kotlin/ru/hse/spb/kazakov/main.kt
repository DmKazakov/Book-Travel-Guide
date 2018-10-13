package ru.hse.spb.kazakov

import com.mongodb.MongoClient
import org.mongodb.morphia.Morphia
import ru.hse.spb.kazakov.nlp.LocationRecognizer

/**
 * Prints all sentences from specified epub file with mentions of locations.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No epub file specified.")
        return
    }
    val epubReader = EpubReader(args[0])
    val pipeliner = LocationRecognizer()

    val morphia = Morphia()
    morphia.mapPackage("ru.hse.spb.kazakov.nlp.LocationContext")
    val datastore = morphia.createDatastore(MongoClient("127.0.0.1", 27017), "BookTravelGuide")
    datastore.ensureIndexes()

    var section = epubReader.readNextSection()
    while (section != null) {
        pipeliner.extractLocations(section).forEach { datastore.save(it) }
        section = epubReader.readNextSection()
    }
}