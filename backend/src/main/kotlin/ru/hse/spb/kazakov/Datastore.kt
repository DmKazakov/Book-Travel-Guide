package ru.hse.spb.kazakov

import com.mongodb.MongoClient
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia

object Datastore {
    const val HOST = "127.0.0.1"
    const val PORT = 27017
    const val DB_NAME = "BookTravelGuide"
    val instance: Datastore

    init {
        val morphia = Morphia()
        morphia.mapPackage("ru.hse.spb.kazakov")
        instance = morphia.createDatastore(MongoClient(HOST, PORT), DB_NAME)
        instance.ensureIndexes()
    }
}