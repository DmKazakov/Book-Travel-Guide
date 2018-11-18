package ru.hse.spb.kazakov

import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore

class BookLocationStore(private val datastore: Datastore) {

    fun save(bookLocation: BookLocation) {
        datastore.save(bookLocation)
    }

    fun getById(id: ObjectId): BookLocation = datastore.get(BookLocation::class.java, id)

    fun getUnreviewedLocations(locationsNumber: Int): List<BookLocation> =
            datastore.createQuery(BookLocation::class.java)
                    .filter("reviewsNumber ==", 0)
                    .limit(locationsNumber)
                    .asList()

    fun getPositiveRateLocations(): List<BookLocation> =
            datastore.createQuery(BookLocation::class.java)
                    .filter("userRating >", 0)
                    .asList()

    fun getNegativeRateLocations(): List<BookLocation> =
            datastore.createQuery(BookLocation::class.java)
                    .filter("userRating <", 0)
                    .asList()
}