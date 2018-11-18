package ru.hse.spb.kazakov

import com.mongodb.MongoClient
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia

object Datastore {
    const val PORT = 27017
    const val DB_NAME = "BookTravelGuide"
    var instance: Datastore? = null

    fun getInstance(host: String): Datastore {
        if (instance == null) {
            val morphia = Morphia()
            morphia.mapPackage("ru.hse.spb.kazakov")
            val instance = morphia.createDatastore(MongoClient(host, PORT), DB_NAME)
            instance.ensureIndexes()
            this.instance = instance
        }

        return instance as Datastore
    }
}