package ru.hse.spb.kazakov

import com.mongodb.MongoClient
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia

class BookLocationStore(dbName: String, host: String, port: Int) {
    private val datastore: Datastore

    init {
        val morphia = Morphia()
        morphia.mapPackage("ru.hse.spb.kazakov")
        datastore = morphia.createDatastore(MongoClient(host, port), dbName)
        datastore.ensureIndexes()
    }

    fun save(bookLocation: BookLocation) {
        datastore.save(bookLocation)
    }

    fun getById(id: ObjectId): BookLocation = datastore.get(BookLocation::class.java, id)
}