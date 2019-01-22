package ru.hse.spb.kazakov.mongo

import com.mongodb.MongoClient
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia

object Datastore {
    private const val PORT = 27017
    private const val DB_NAME = "Test"
    private lateinit var instance: Datastore

    fun getInstance(host: String): Datastore {
        if (!this::instance.isInitialized) {
            val morphia = Morphia()
            morphia.mapPackage("ru.hse.spb.kazakov.mongo")
            instance = morphia.createDatastore(MongoClient(host, PORT), DB_NAME)
            instance.ensureIndexes()
        }

        return instance
    }
}