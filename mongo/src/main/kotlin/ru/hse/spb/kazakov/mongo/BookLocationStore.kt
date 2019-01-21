package ru.hse.spb.kazakov.mongo

import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore

private const val QUOTES_LIMIT = 100

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

    fun getOutgoingAmodLocations(): List<BookLocation> =
            datastore.createQuery(BookLocation::class.java)
                    .filter("outgoingAmod >", 0)
                    .order("-outgoingAmod")
                    .limit(QUOTES_LIMIT)
                    .asList()

    fun getNeighborsAdjLocations(): List<BookLocation> =
            datastore.createQuery(BookLocation::class.java)
                    .filter("neighborsAmod >", 0)
                    .order("-neighborsAmod")
                    .limit(QUOTES_LIMIT)
                    .asList()

    fun getSentimentLocations(): List<BookLocation> =
            datastore.createQuery(BookLocation::class.java)
                    .filter("sentiment ==", 2)
                    .limit(QUOTES_LIMIT)
                    .asList()

    fun getAllLocations(): List<BookLocation> =
            datastore.createQuery(BookLocation::class.java)
                    .asList()

    fun deleteQuote(id: ObjectId) = datastore.delete(BookLocation::class.java, id)
}