package ru.hse.spb.kazakov

import com.mongodb.MongoClient
import org.junit.Test
import org.mongodb.morphia.Morphia
import ru.hse.spb.kazakov.nlp.LocationContext

class EpubParserTest {
    @Test
    fun server() {
        val morphia = Morphia()
        morphia.mapPackage("ru.hse.spb.kazakov.nlp.LocationContext")
        val datastore = morphia.createDatastore(MongoClient("127.0.0.1", 27017), "BookTravelGuide")
        datastore.ensureIndexes()
        val query = datastore.createQuery(LocationContext::class.java)
        val locs = query.asList()
        locs.forEach {
            println(it.location)
            println(it.sentence)
            println(it.inDeps)
            println(it.outDeps)
            println(it.rightNeighbors)
            println(it.leftNeighbors)
            println()
        }
    }
}